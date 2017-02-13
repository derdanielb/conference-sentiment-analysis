package net.csa.tweet.analysis;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.kafka.ConsumerMessage;
import akka.kafka.ConsumerSettings;
import akka.kafka.ProducerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.kafka.javadsl.Producer;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import scala.Float;
import scala.util.Try;

import java.io.File;
import java.time.Instant;
import java.util.Objects;
import java.util.concurrent.*;

public class TweetAnalysis {
    private static final Logger log = LoggerFactory.getLogger(TweetAnalysis.class);

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

        final Flow<ConsumerMessage.CommittableMessage<String, String>, Pair<String, String>, NotUsed> kafkaStringFlow =
                Flow.fromFunction(committableMessagePair -> Pair.create(committableMessagePair.record().key(),
                        committableMessagePair.record().value()));

        Sink<Object, CompletionStage<Done>> sink
                = Sink.foreach(p -> log.info("BAUM42: " + p.toString()));

        // ----- construct the processing graph as required using shapes obtained for stages -----

        SharedKillSwitch killSwitch = KillSwitches.shared("baumKiller");
        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for kafka
            final SourceShape<ConsumerMessage.CommittableMessage<String, String>> kafkaSourceShape = b.add(kafkaSource);

            // flow shape to convert messages to strings
            final FlowShape<ConsumerMessage.CommittableMessage<String, String>, Pair<String, String>> kafkaStringFlowShape = b.add(kafkaStringFlow);

            final UniformFanOutShape<Pair<String, String>, Pair<String, String>> broad = b.add(Broadcast.create(1));

            final UniformFanInShape<Pair<String, String>, Pair<String, String>> merge = b.add(Merge.create(1));

            final FlowShape<Object, Object> killShape = b.add(killSwitch.flow());

            b.from(kafkaSourceShape)
                    .via(kafkaStringFlowShape)
                    .viaFanOut(broad);

            b.from(broad)
                    .viaFanIn(merge);

            b.from(merge)
                    .via(killShape)
                    .to(s);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();

        try {
            Thread.sleep(5000);//TODO adjust time!
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

}