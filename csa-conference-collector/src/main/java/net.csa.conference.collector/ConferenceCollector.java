package net.csa.conference.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author philipp.amkreutz
 */
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


		final Source<String, CompletionStage<IOResult>> conferenceSource;
		{
			// source for streaming the input file
//			final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile("/Users/philippamkreutz/Documents/HBRS/OOKA/Uebungen/Uebung7/conferences.csv")
			final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File(args[1]))
					.log("csa-conference-collector-fileSource");

			// each line is used as a hastag thus split by line separator
			final Flow<ByteString, String, NotUsed> framingFlow
					= Framing.delimiter(ByteString.fromString(System.lineSeparator()), 1000, FramingTruncation.ALLOW)
					.map(ByteString::utf8String)
					.log("csa-conference-collector-framingFlow");

			conferenceSource = fileSource.via(framingFlow)
					.log("csa-conference-collector-conferenceSource");
		}

		final Flow<String, Conference, NotUsed> conferenceMappingFlow;
		{
			Flow<String, Conference, NotUsed> flow =
					Flow.fromFunction(p -> {

						//TODO: MAPPING!!!
						return new Conference();


					});
			conferenceMappingFlow = flow.log("csa-conference-collector-conferenceMappingFlow");
		}

		final Sink<Conference, CompletionStage<Done>> sink =
				Sink.foreach(p -> {

				});

		final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

			// source shape for hashtags
			final SourceShape<String> conferenceSourceShape = b.add(conferenceSource);
			final FlowShape<String, Conference> conferenceMappingFlowShape = b.add(conferenceMappingFlow);

			b.from(conferenceSourceShape)
					.via(conferenceMappingFlowShape)
					.to(s);

			return ClosedShape.getInstance();
		});

		CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
		CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();
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
