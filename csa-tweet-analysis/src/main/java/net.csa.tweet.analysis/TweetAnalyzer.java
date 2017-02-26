package net.csa.tweet.analysis;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.Pair;
import akka.japi.function.Function;
import akka.kafka.ConsumerSettings;
import akka.kafka.Subscriptions;
import akka.kafka.javadsl.Consumer;
import akka.stream.*;
import akka.stream.javadsl.*;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author philipp.amkreutz
 */
public class TweetAnalyzer {

	private static final Logger log = LoggerFactory.getLogger(TweetAnalyzer.class);

	private static TweetAnalyzerResult result;

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
			tweetSource = Consumer.committableSource(consumerSettings, Subscriptions.topics("tweet-topic")).map(cm -> cm.record().value());
		}

		final Flow<String, Pair<List<Tweet>, Integer>, NotUsed> tweetMappingFlow;
		{
			Flow<String,Pair<List<Tweet>, Integer>, NotUsed> flow =
					Flow.fromFunction(p -> {
						JsonParser jsonParser = new JsonParser();
						JsonObject jsonObject = (JsonObject) jsonParser.parse(p);
						int id = jsonObject.get("id").getAsInt();
						List<Tweet> tweetList = new ArrayList<>();
						String tweet = "";
						int i = 0;
						while(tweet != null) {
							try {
								tweet = jsonObject.get("tweet" + i).getAsString();
								tweetList.add(new Tweet(tweet));
								i++;
							} catch (NullPointerException e) {
								tweet = null;
							}
						}
						return Pair.create(tweetList, id);
					});
			tweetMappingFlow = flow.log("csa-tweet-analyzer-tweetMappingFlow");
		}

		final Flow<Pair<List<Tweet>, Integer>, TweetAnalysis, NotUsed> tweetAnalysisFlow;
		{
			Flow<Pair<List<Tweet>, Integer>, TweetAnalysis, NotUsed> flow =
					Flow.fromFunction(p -> {
						TweetAnalysis tweetAnalysis = new TweetAnalysis(p.second());
						SentimentAnalysis sentimentAnalysis = new MeaningCloudSentimentAnalysis();
						for(Tweet tweet : p.first()){
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							tweet = sentimentAnalysis.analyzeTweet(tweet);
							tweetAnalysis.addTweet(tweet);
						}
						return tweetAnalysis;
					});
			tweetAnalysisFlow = flow.log("csa-tweet-analyzer-tweetAnalysisFlow");

		}

		final Flow<TweetAnalysis, NotUsed, NotUsed> tweetAnalyserResultFlow;
		{
			Flow<TweetAnalysis, NotUsed, NotUsed> flow =
					Flow.fromFunction(p -> {
						if(result == null) {
							result = new TweetAnalyzerResult();
						}
						result.addTweetAnalysis(p);
						return NotUsed.getInstance();
					});
			tweetAnalyserResultFlow = flow.log("csa-tweet-analyzer-tweetAnalyzerResultFlow");

		}

		final Sink<NotUsed, CompletionStage<Done>> sink = Sink.foreach(p -> {
			System.out.println("---------------------------------------------------");
			System.out.println("RESULT");
			result.printResult();
			System.out.println("---------------------------------------------------");
		});

		final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

			// source shape for hashtags
			final SourceShape<String> tweetSourceShape = b.add(tweetSource);
			final FlowShape<String, Pair<List<Tweet>, Integer>> tweetMappingFlowShape = b.add(tweetMappingFlow);
			final FlowShape<Pair<List<Tweet>, Integer>, TweetAnalysis> tweetAnalysisFlowShape = b.add(tweetAnalysisFlow);
			final FlowShape<TweetAnalysis, NotUsed> tweetAnalyserResultFlowShape = b.add(tweetAnalyserResultFlow);

			b.from(tweetSourceShape)
					.via(tweetMappingFlowShape)
					.via(tweetAnalysisFlowShape)
					.via(tweetAnalyserResultFlowShape)
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
