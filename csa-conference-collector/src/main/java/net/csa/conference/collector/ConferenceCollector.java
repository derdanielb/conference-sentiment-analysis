package net.csa.conference.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.Http;
import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import net.csa.conference.model.Conference;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.util.Try;

import java.io.File;
import java.util.concurrent.*;

public class ConferenceCollector {
    private static final Logger log = LoggerFactory.getLogger(ConferenceCollector.class);

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

        // source for conferences
        final Source<String, CompletionStage<IOResult>> conferenceSource;
        {
            // source for streaming the input file
            final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File(args[0]))
                    .log("csa-conference-collector-fileSource");

            // each line is used as a conference thus split by line separator
            final Flow<ByteString, String, NotUsed> framingFlow
                    = Framing.delimiter(ByteString.fromString(System.lineSeparator()), 1000, FramingTruncation.ALLOW)
                    .map(ByteString::utf8String)
                    .log("csa-conference-collector-framingFlow");

            conferenceSource = fileSource.via(framingFlow)
                    .log("csa-conference-collector-conferenceSource");
        }

        final Flow<String, Conference, NotUsed> conferenceConversionFlow;
        {
            conferenceConversionFlow = Flow.fromFunction(s -> new Conference());
        }

        // flow to create a http request to our twitter search
        final Flow<Conference, Pair<HttpRequest, String>, NotUsed> createRequestFlow;
        {
            Flow<Conference, String, NotUsed> jsonFlow = Flow.fromFunction(conference -> {
                ObjectMapper mapper = new ObjectMapper();
                return mapper.writeValueAsString(conference);
            });

            Flow<String, Pair<HttpRequest, String>, NotUsed> httpFlow  = Flow.fromFunction(
                    str -> Pair.create(HttpRequest
                            .POST("http://localhost:8082/csa-conference/v1/conferences")
                            .withEntity(ContentTypes.APPLICATION_JSON, str),
                            str));

            createRequestFlow = jsonFlow
                    .via(httpFlow)
                    .log("csa-conference-collector-createRequestFlow");
        }

        // flow to actually execute each http request to the twitter flow using Akka HTTP client-side
        final Flow<Pair<HttpRequest, String>, Try<HttpResponse>, NotUsed> httpClientFlow;
        {
            Flow<Pair<HttpRequest, String>, Pair<Try<HttpResponse>, String>, NotUsed> flow
                    = Http.get(system).superPool(materializer);
            httpClientFlow = flow
                    .map(Pair::first)
                    .log("csa-conference-collector-httpClientFlow");
        }

        Sink<Try<HttpResponse>, CompletionStage<Done>> sink = Sink.foreach(reply -> {
            HttpResponse httpResponse = reply.get();
            log.info("Status Code " + httpResponse.status());
            httpResponse.entity()
                    .toStrict(42, materializer)
                    .toCompletableFuture()
                    .get();
        });

        // ----- construct the processing graph as required using shapes obtained for stages -----

        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for conferences
            final SourceShape<String> conferenceSourceShape = b.add(conferenceSource);

            //convert
            final FlowShape<String, Conference> conferenceFlowShape = b.add(conferenceConversionFlow);

            // flow shape to create an HttpRequest for every conference
            final FlowShape<Conference, Pair<HttpRequest, String>> createRequestFlowShape = b.add(createRequestFlow);

            // flow shape to apply Akka HTTP client-side in the processing graph to call csa-conference for each conference
            final FlowShape<Pair<HttpRequest, String>, Try<HttpResponse>> httpClientFlowShape = b.add(httpClientFlow);

            b.from(conferenceSourceShape)
                    .via(conferenceFlowShape)
                    .via(createRequestFlowShape)
                    .via(httpClientFlowShape)
                    .to(s);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();
        // TODO if you process a lot remove this waiting for the result and instead loop and sleep until completed
        Done done = completableFuture.get(30, TimeUnit.SECONDS); // awaiting the future to complete with a timeout

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
