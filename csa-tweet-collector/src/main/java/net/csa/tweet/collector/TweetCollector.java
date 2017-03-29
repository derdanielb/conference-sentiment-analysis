package net.csa.tweet.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.*;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.kafka.ProducerSettings;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import akka.stream.ClosedShape;
import akka.stream.FanInShape2;
import akka.stream.FlowShape;
import akka.stream.Graph;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.SourceShape;
import akka.stream.Supervision;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.GraphDSL;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.javadsl.ZipWith;
import akka.util.ByteString;
import net.csa.tweet.collector.model.Tweet;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.util.Try;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class TweetCollector {

    private static final Logger log = LoggerFactory.getLogger(TweetCollector.class);

    public static void main(String args[]) throws InterruptedException, TimeoutException, ExecutionException {

        if(args.length != 1) {
            log.info("Start the job with exactly one parameter for the path of the csv-file with the hashtags.");
            System.exit(1);
        }

        String pathToFile = args[0];
        log.info("Path to the csv file: " + pathToFile);

        // this decider applies the default supervision strategy, but it adds some logging in case of an error
        final Function<Throwable, Supervision.Directive> decider = ex -> {
            log.error("Error during stream processing.", ex);
            return Supervision.stop();
        };

        // actor system and materializer always needed to use Akka Streams
        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(ActorMaterializerSettings.create(system)
                .withSupervisionStrategy(decider), system);

        // ----- construct the stages for the processing graph -----

        // source for hashtags
        final Source<String, CompletionStage<IOResult>> hashtagSource;
        {
            // source for streaming the input file
            final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File(pathToFile))
                    .log("csa-tweet-collector-fileSource");

            // each line is used as a hashtag thus split by line separator
            final Flow<ByteString, String, NotUsed> framingFlow
                    = Framing.delimiter(ByteString.fromString(System.lineSeparator()), 1000, FramingTruncation.ALLOW)
                    .map(ByteString::utf8String)
                    .log("csa-tweet-collector-framingFlow");

            hashtagSource = fileSource.via(framingFlow)
                    .log("csa-tweet-collector-hashtagSource");
        }

        // source for numbering elements in the stream
        final Source<Integer, NotUsed> integerSource = Source.range(0, Integer.MAX_VALUE - 1)
                .log("csa-tweet-collector-numberSource");

        // fan in to assign an integer to each hashtag
        final Graph<FanInShape2<String, Integer, Pair<String, Integer>>, NotUsed> zip = ZipWith.create(Pair::new);

        // flow to create a http request to our twitter search
        final Flow<Pair<String, Integer>, Pair<HttpRequest, Integer>, NotUsed> createRequestFlow;
        {
            Flow<Pair<String, Integer>, Pair<HttpRequest, Integer>, NotUsed> flow
                    = Flow.fromFunction(p -> new Pair<>(HttpRequest
                    .create("http://localhost:8090//twitter/search/" + p.first()),
                    p.second()));
            createRequestFlow = flow.log("csa-tweet-collector-createRequestFlow");
        }

        // flow to actually execute each http request to the twitter flow using Akka HTTP client-side
        final Flow<Pair<HttpRequest, Integer>, Pair<Try<HttpResponse>, Integer>, NotUsed> httpClientFlow;
        {
            Flow<Pair<HttpRequest, Integer>, Pair<Try<HttpResponse>, Integer>, NotUsed> flow
                    = Http.get(system).superPool(materializer);
            flow = flow
                    .map(p -> {
                        log.info("[httpClientFlow] Status Code " + p.first().get().status() + " - Element: " + p.second());
                        return p;
                    });
            //flow.filter(p -> p.first().get().status() == StatusCodes.OK);
            httpClientFlow = flow.log("csa-tweet-collector-httpClientFlow");
        }

        // flow to process the HttpResponse with the twitter tweets
        final Flow<Pair<Try<HttpResponse>, Integer>, Pair<List<Tweet>, Integer>, NotUsed> httpResponseFlow;
        {
            Flow<Pair<Try<HttpResponse>, Integer>, Pair<List<Tweet>, Integer>, NotUsed> flow
                    = Flow.fromFunction(p -> {
                        HttpEntity.Strict httpEntity = p.first().get().entity().toStrict(5, materializer).toCompletableFuture().get();
                        String tweets = httpEntity.getData().utf8String();
                        log.info("[httpResponseFlow] Element: " + p.second() + ", Tweets: " + tweets);

                        List<Tweet> tweetsList= new ArrayList<Tweet>();
                        JSONArray jsonTweets = new JSONArray(tweets);
                        for(int i = 0; i < jsonTweets.length(); i++) {
                            tweetsList.add(new Tweet(jsonTweets.getString(i)));
                        }

                        return Pair.create(tweetsList, p.second());
                    }
            );
            httpResponseFlow = flow;
        }

        // create kafka producer for the sink
        KafkaProducer<String, String> kafkaProducer = ProducerSettings
                .create(system, new StringSerializer(), new StringSerializer())
                .withBootstrapServers("localhost:19092")
                .createKafkaProducer();

        // sink to write tweets in kafka topic
        Sink<Pair<List<Tweet>, Integer>, CompletionStage<Done>> sink
                = Sink.foreach(p -> {

                    // transform list of tweets in json array
                    String tweets = "[";
                    for(int i = 0; i < p.first().size(); i++) {
                        tweets += "\"" + p.first().get(i).getMessage() + "\"";
                        if(i < p.first().size() - 1) tweets += ",";
                        else tweets += "]";
                    }

                    // write the json tweet array to kafka topic tweets
                    kafkaProducer.send(new ProducerRecord<String, String>("tweets", tweets));

                    log.info("[Sink] Element: " + p.second() + " with " + p.first().size() + " tweets");
                    log.info("[Sink] kafka: " + tweets);

        });


        // ----- construct the processing graph as required using shapes obtained for stages -----

        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for hashtags
            final SourceShape<String> hashtagSourceShape = b.add(hashtagSource);

            // source shape for integers
            final SourceShape<Integer> integerSourceShape = b.add(integerSource);

            // fan in shape to assign an integer to each hashtag
            final FanInShape2<String, Integer, Pair<String, Integer>> zipShape = b.add(zip);

            // flow shape to create an HttpRequest for every hashtag
            final FlowShape<Pair<String, Integer>, Pair<HttpRequest, Integer>> createRequestFlowShape = b.add(createRequestFlow);

            // flow shape to apply Akka HTTP client-side in the processing graph to call csa-twitter-search for each hashtag
            final FlowShape<Pair<HttpRequest, Integer>, Pair<Try<HttpResponse>, Integer>> httpClientFlowShape = b.add(httpClientFlow);

            // flow shape to get the twitter tweets for each hashtag out of the HTTPResponse
            final FlowShape<Pair<Try<HttpResponse>, Integer>, Pair<List<Tweet>, Integer>> httpResponseFlowShape = b.add(httpResponseFlow);


            b.from(hashtagSourceShape).toInlet(zipShape.in0());
            b.from(integerSourceShape).toInlet(zipShape.in1());

            b.from(zipShape.out())
                    .via(createRequestFlowShape)
                    .via(httpClientFlowShape)
                    .via(httpResponseFlowShape)
                    .to(s);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();

        //Done done = completableFuture.get(30, TimeUnit.SECONDS); // awaiting the future to complete with a timeout
        // wait for completition
        while(!completableFuture.isDone()) {
            Thread.sleep(100);
        }

        // log the overall status
        if (completableFuture.isDone()) {
            log.info("Done.");
        }
        if (completableFuture.isCompletedExceptionally()) {
            log.warn("Completed exceptionally.");
        }
        if (completableFuture.isCancelled()) {
            log.warn("Canceled.");
        }

        // trigger graceful shutdown of the actor system
        system.shutdown();
        Thread.sleep(500);
    }

}
