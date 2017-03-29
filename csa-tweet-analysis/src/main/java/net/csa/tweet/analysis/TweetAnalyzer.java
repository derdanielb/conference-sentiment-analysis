package net.csa.tweet.analysis;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import akka.stream.ClosedShape;
import akka.stream.Graph;
import akka.stream.Materializer;
import akka.stream.SourceShape;
import akka.stream.Supervision;
import akka.stream.javadsl.GraphDSL;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class TweetAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(TweetAnalyzer.class);

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

        // source for kafka

        // source for numbering elements in the stream
        final Source<Integer, NotUsed> integerSource = Source.range(0, 10)
                .log("csa-tweet-collector-numberSource");

        final Sink<Integer, CompletionStage<Done>> sink =
                Sink.foreach(i -> log.info(i.toString()));

        // ----- construct the processing graph as required using shapes obtained for stages -----

        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for integer
            final SourceShape<Integer> integerSourceShape = b.add(integerSource);

            b.from(integerSourceShape).to(s);

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
