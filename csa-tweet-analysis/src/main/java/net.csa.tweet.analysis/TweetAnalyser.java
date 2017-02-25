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

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * @author philipp.amkreutz
 */
public class TweetAnalyser {

	private static final Logger log = LoggerFactory.getLogger(TweetAnalyser.class);

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

		final ConsumerSettings<String, String> consumerSettings = ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
				.withBootstrapServers("192.168.1.24:19092")
				.withGroupId("analyser1")
				.withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
				.withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");

		final Source<String, Consumer.Control> tweetSource;
		{
			tweetSource = Consumer.committableSource(consumerSettings, Subscriptions.topics("tweet-topic")).map(cm -> cm.record().value())
					.log("csa-tweet-analyser-tweetSource");
		}

		final Sink<String, CompletionStage<Done>> sink = Sink.foreach(p -> {
			log.info("Sink: " + p);
		});

		final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

			// source shape for hashtags
			final SourceShape<String> tweetSourceShape = b.add(tweetSource);

			b.from(tweetSourceShape).to(s);

			return ClosedShape.getInstance();
		});

		CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
		CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();
		// TODO if you process a lot remove this waiting for the result and instead loop and sleep until completed
		while (!completableFuture.isDone()) {

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
