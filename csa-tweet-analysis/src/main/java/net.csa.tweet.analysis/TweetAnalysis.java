package net.csa.tweet.analysis;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.kafka.ConsumerMessage;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.*;
import akka.stream.javadsl.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.neural.rnn.RNNCoreAnnotations;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.util.CoreMap;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class TweetAnalysis {
    private static final Logger log = LoggerFactory.getLogger(TweetAnalysis.class);

    static StanfordCoreNLP pipeline = new StanfordCoreNLP("MyPropFile.properties");

    public static void main(String args[]) throws InterruptedException, TimeoutException, ExecutionException {

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

        final ConsumerSettings<String, String> consumerSettings =
                ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
                        .withBootstrapServers("localhost:19092")
                        .withGroupId("csa-twitter-analysis")
                        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                        .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

        Source<ConsumerMessage.CommittableMessage<String, String>, Consumer.Control> kafkaSource =
                Consumer.committableSource(consumerSettings, Subscriptions.topics("tc-topic-0",
                        "tc-topic-1",
                        "tc-topic-2",
                        "tc-topic-3",
                        "tc-topic-4",
                        "tc-topic-5",
                        "tc-topic-6",
                        "tc-topic-7",
                        "tc-topic-8",
                        "tc-topic-9"))
                //.map(msg -> new Pair<>(msg, msg.record().value()));
                .mapAsync(1, msg -> CompletableFuture.supplyAsync(() -> {
                    try {
                        return msg;
                    } catch (Exception e) {
                        return null;
                    }
                }));


        final Flow<ConsumerMessage.CommittableMessage<String, String>, Pair<String, List<String>>, NotUsed> kafkaStringFlow =
                Flow.fromFunction((ConsumerMessage.CommittableMessage<String, String> committableMessagePair) ->
                        Pair.create(committableMessagePair.record().key(), committableMessagePair.record().value()))
                .map(p -> {
                    Gson gson = new Gson();
                    return Pair.create(p.first(), gson.fromJson(p.second(), new TypeToken<List<String>>(){}.getType()));
                });

        Sink<Object, CompletionStage<Done>> sink
                = Sink.foreach(p -> log.info(p.toString()));

        // ----- construct analysis -----

        //tweet count
        final Flow<Pair<String, List<String>>, Pair<String, Integer>, NotUsed> tweetCount =
                Flow.fromFunction((Pair<String, List<String>> p) -> Pair.create(p.first(), p.second().size()));

        //tweet count
        final Flow<Pair<String, Integer>, String, NotUsed> tweetCountInfo = Flow.fromFunction(p -> p.first() + ": " + p.second() + " tweets");

        //word count
        final Flow<Pair<String, List<String>>, String, NotUsed> wordCountFlow =
                Flow.fromFunction((Pair<String, List<String>> p) ->
                        p.first() + ": " + String.join(" ", p.second()).split(" ").length + " words");

        //tweet ranking
        final Flow<Pair<String, Integer>, String, NotUsed> tweetRanking =
                Flow.<Pair<String, Integer>>create()
                .grouped(10)
                .map(pairs -> {
                    List<Pair<String, Integer>> sortList = new ArrayList<>(pairs);
                    sortList.sort(Comparator.comparingInt(Pair::second));
                    Collections.reverse(sortList);
                    return sortList;
                }).map(pairs -> {
                    List<String> result = new ArrayList<>();
                    for (Pair<String, Integer> pair : pairs)
                        result.add(pair.first() + " (" + pair.second() + ")");
                    return result;
                }).map(t -> "Tweet Count Ranking: " + String.join(", ", t));

        //Sentiment Analysis
        final Flow<Pair<String, List<String>>, Pair<String, Double>, NotUsed> sentimentAnalysisFlow =
                Flow.fromFunction((Pair<String, List<String>> p) -> {
                    List<Integer> l = new ArrayList<>();
                    for (String tweet : p.second()) {
                        l.add(findSentiment(tweet));
                    }
                    return Pair.create(p.first(), l);
                }).map(p -> {
                    double x = 0;
                    for(int i: p.second())
                        x += i;
                    return Pair.create(p.first(), x/p.second().size());
                });

        Flow<Pair<String, Double>, String, NotUsed> averageSentimentLevelFlow =
                Flow.fromFunction((Pair<String, Double> p) -> p.first() + ": average sentiment level is " + p.second());

        // ----- construct the processing graph as required using shapes obtained for stages -----

        SharedKillSwitch killSwitch = KillSwitches.shared("baumKiller");
        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for kafka
            final SourceShape<ConsumerMessage.CommittableMessage<String, String>> kafkaSourceShape = b.add(kafkaSource);

            // flow shape to convert messages to strings
            final FlowShape<ConsumerMessage.CommittableMessage<String, String>, Pair<String, List<String>>> kafkaStringFlowShape = b.add(kafkaStringFlow);

            final UniformFanOutShape<Pair<String, List<String>>, Pair<String, List<String>>> broad = b.add(Broadcast.create(3));

            final UniformFanInShape<String, String> merge = b.add(Merge.create(5));

            final FlowShape<Object, Object> killShape = b.add(killSwitch.flow());
            
            b.from(kafkaSourceShape)
                    .via(kafkaStringFlowShape)
                    .viaFanOut(broad);

            b.from(merge)
                    .via(killShape)
                    .to(s);

            //analyser

            final UniformFanOutShape<Pair<String, Integer>, Pair<String, Integer>> countCast = b.add(Broadcast.create(2));
            final UniformFanOutShape<Pair<String, Double>, Pair<String, Double>> sentimentCast = b.add(Broadcast.create(2));

            final FlowShape<Pair<String, List<String>>, Pair<String, Integer>> tweetCountShape = b.add(tweetCount);

            final FlowShape<Pair<String, Integer>, String> tweetCountInfoShape = b.add(tweetCountInfo);

            final FlowShape<Pair<String, List<String>>, String> wordCountShape = b.add(wordCountFlow);

            final FlowShape<Pair<String, Integer>, String> tweetRankingShape = b.add(tweetRanking);

            final FlowShape<Pair<String, List<String>>, Pair<String, Double>> sentimentAnalysisShape = b.add(sentimentAnalysisFlow);

            final FlowShape<Pair<String, Double>, String> averageSentimentLevelShape = b.add(averageSentimentLevelFlow);

            //count
            b.from(broad)
                    .via(tweetCountShape)
                    .viaFanOut(countCast);

            //count log
            b.from(countCast)
                    .via(tweetCountInfoShape)
                    .viaFanIn(merge);

            //word count
            b.from(broad)
                    .via(wordCountShape)
                    .viaFanIn(merge);

            //count rank
            b.from(countCast)
                    .via(tweetRankingShape)
                    .viaFanIn(merge);

            //sentiment Analysis
            b.from(broad)
                    .via(sentimentAnalysisShape)
                    .viaFanOut(sentimentCast);

            //sentiment average
            b.from(sentimentCast)
                    .via(averageSentimentLevelShape)
                    .viaFanIn(merge);

            //sentiment ranking
            b.from(sentimentCast)
                    .via(null)
                    .viaFanIn(merge);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();

        try {
            Thread.sleep(10000);//TODO adjust time!
            killSwitch.shutdown();
            completableFuture.get(5, TimeUnit.SECONDS); // awaiting the future to complete with a timeout
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
        } catch(Throwable t) {
            log.warn("Killing by timeout ¯\\_(ツ)_/¯");
            killSwitch.abort(t);
        }

        // trigger graceful shutdown of the actor system
        system.shutdown();
        Thread.sleep(500);
    }

    public static int findSentiment(String tweet) {

        int mainSentiment = 0;
        if (tweet != null && tweet.length() > 0) {
            int longest = 0;
            Annotation annotation = pipeline.process(tweet);
            for (CoreMap sentence : annotation
                    .get(CoreAnnotations.SentencesAnnotation.class)) {
                Tree tree = sentence
                        .get(SentimentCoreAnnotations.SentimentAnnotatedTree.class);
                int sentiment = RNNCoreAnnotations.getPredictedClass(tree);
                String partText = sentence.toString();
                if (partText.length() > longest) {
                    mainSentiment = sentiment;
                    longest = partText.length();
                }

            }
        }
        return mainSentiment;
    }
}