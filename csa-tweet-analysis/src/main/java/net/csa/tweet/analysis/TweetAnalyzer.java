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
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.GraphDSL;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.stream.scaladsl.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

public class TweetAnalyzer {

    private static final Logger log = LoggerFactory.getLogger(TweetAnalyzer.class);

    public static void main(String args[]) throws InterruptedException, TimeoutException, ExecutionException {

        final String[] kafkaTopics = {
                "tweets-javaland",
                "tweets-scaladays",
                "tweets-doag2016",
                "tweets-froscon",
                "tweets-jaxcon",
                "tweets-swqd16",
                "tweets-swqd17",
                "tweets-iot16",
                "tweets-iot17"
        };

        // this decider applies the default supervision strategy, but it adds some logging in case of an error
        final Function<Throwable, Supervision.Directive> decider = ex -> {
            log.error("Error during stream processing.", ex);
            return Supervision.stop();
        };

        // actor system and materializer always needed to use Akka Streams
        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(ActorMaterializerSettings.create(system)
                .withSupervisionStrategy(decider), system);

        //Consumer Settings for Kafka
        ConsumerSettings<String, String> consumerSettings =
                ConsumerSettings.create(system, new StringDeserializer(), new StringDeserializer())
                        .withBootstrapServers("localhost:19092")
                        .withGroupId("tweetAnalysis")
                        .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")
                        .withProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");


        // ----- construct the stages for the processing graph -----

        // source for kafka
        final Source<ConsumerMessage.CommittableMessage<String, String>, Consumer.Control> kafkaSource =
                Consumer.committableSource(consumerSettings, Subscriptions.topics(kafkaTopics))
                        .mapAsync(1, msg -> CompletableFuture.supplyAsync(() -> {
                            try {
                                return msg;
                            } catch (Exception e) {
                                return null;
                            }
                        }))
                        .log("csa-tweet-analyzer-kafkaSource");


        //Flow to convert the json-tweet array from kafka into a ConferenceAnalysis Object
        final Flow<ConsumerMessage.CommittableMessage<String, String>, ConferenceAnalysis, NotUsed> kafkaConvertStringFlow =
                Flow.fromFunction((ConsumerMessage.CommittableMessage<String, String> committableMessagePair) ->
                        Pair.create(committableMessagePair.record().key(), committableMessagePair.record().value()))
                        .map(p -> {
                            Gson gson = new Gson();
                            List<String> list = gson.fromJson(p.second(), new TypeToken<List<String>>(){}.getType());
                            String hashtag = list.get(0);
                            list = list.subList(1,list.size()-1);
                            log.info("[kafkaConvertStringFlow] String converted to ConferenceAnalysis object (hashtag: #" + hashtag + " with " + list.size() + " tweets)");
                            return new ConferenceAnalysis(hashtag, list);
                        });


        //Flow to count words
        final Flow<ConferenceAnalysis, ConferenceAnalysis, NotUsed> wordCountFlow =
                Flow.fromFunction(conferenceAnalysis -> {
                    int count = 0;
                    for(int i=0; i<conferenceAnalysis.getTweetCount(); i++) {
                        String tweet = conferenceAnalysis.getTweets().get(i);
                        if(tweet != null) {
                            String[] split = tweet.split(" ");
                            count += split.length;
                        }
                    }
                    conferenceAnalysis.setWordCount(count);
                    return conferenceAnalysis;
                });


        //Flow for sentiment analysis
        //rank tweets into very positive, positiv, neutral, negative or very negative
        //and create a score-value with this ranking
        final Flow<ConferenceAnalysis, ConferenceAnalysis, NotUsed> sentimentAnalysisFlow =
                Flow.fromFunction(conferenceAnalysis -> {

                    ScoreAnalyser scoreAnalyser = new ScoreAnalyser();

                    for (String tweet: conferenceAnalysis.getTweets()) {

                        int score = scoreAnalyser.findSentiment(tweet);
                        conferenceAnalysis.changeScore(score);
                        if(score < 2) conferenceAnalysis.incrementNegativeCount();
                        if(score == 2) conferenceAnalysis.incrementNeutralCount();
                        if(score > 2) conferenceAnalysis.incrementPositiveCount();

                    }

                    log.info("[SentimentAnalysisFlow] hashtag #" + conferenceAnalysis.getHashtag() + ": score = " + conferenceAnalysis.getScore());

                    return conferenceAnalysis;
                });


        //Flow to create rankings
        final Flow<ConferenceAnalysis, List<ConferenceAnalysis>, NotUsed> createRankingsFlow =
                Flow.<ConferenceAnalysis>create().grouped(kafkaTopics.length)
                        .map(list -> {

                            List<ConferenceAnalysis> rankingList = new ArrayList<>(list);
                            log.info("[createRankingsFlow] Create rankings");
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");
                            int rank = 1;

                            //ranking 1 - tweetCount
                            Collections.sort(rankingList, Comparator.comparingInt(ConferenceAnalysis::getTweetCount));
                            Collections.reverse(rankingList);
                            log.info("[createRankingsFlow] Ranking by tweetCount");
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");
                            rank = 1;
                            for(ConferenceAnalysis conferenceAnalysis: rankingList) {
                                log.info("[createRankingsFlow] " + rank++ + ".\t " + conferenceAnalysis.getTweetCount()
                                    + "\t #" + conferenceAnalysis.getHashtag());
                            }
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");

                            //ranking 2 - wordCount
                            Collections.sort(rankingList, Comparator.comparingInt(ConferenceAnalysis::getWordCount));
                            Collections.reverse(rankingList);
                            log.info("[createRankingsFlow] Ranking by wordCount");
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");
                            rank = 1;
                            for(ConferenceAnalysis conferenceAnalysis: rankingList) {
                                log.info("[createRankingsFlow] " + rank++ + ".\t " + conferenceAnalysis.getWordCount()
                                        + "\t #" + conferenceAnalysis.getHashtag());
                            }
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");

                            //ranking 3 - positiveCount
                            Collections.sort(rankingList, Comparator.comparingInt(ConferenceAnalysis::getPositiveCount));
                            Collections.reverse(rankingList);
                            log.info("[createRankingsFlow] Ranking by positiveCount");
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");
                            rank = 1;
                            for(ConferenceAnalysis conferenceAnalysis: rankingList) {
                                log.info("[createRankingsFlow] " + rank++ + ".\t " + conferenceAnalysis.getPositiveCount()
                                        + "\t #" + conferenceAnalysis.getHashtag());
                            }
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");

                            //ranking 4 - score (cumulated score value - positive/neutral/negative tweets)
                            Collections.sort(rankingList, Comparator.comparingInt(ConferenceAnalysis::getScore));
                            Collections.reverse(rankingList);
                            log.info("[createRankingsFlow] Ranking by score");
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");
                            rank = 1;
                            for(ConferenceAnalysis conferenceAnalysis: rankingList) {
                                log.info("[createRankingsFlow] " + rank++ + ".\t " + conferenceAnalysis.getScore()
                                        + "\t #" + conferenceAnalysis.getHashtag());
                            }
                            log.info("[createRankingsFlow] -----------------------------------------------------------------");

                            return list;
                        });



        //sink to log the list of tweets
        final Sink<List<ConferenceAnalysis>, CompletionStage<Done>> sink =
                Sink.foreach(list -> {

                    list.forEach(item -> log.info("[Sink] " + item.toString()));
                });



        // ----- construct the processing graph as required using shapes obtained for stages -----

        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for integer
            final SourceShape<ConsumerMessage.CommittableMessage<String, String>> kafkaSourceShape = b.add(kafkaSource);

            final FlowShape<ConsumerMessage.CommittableMessage<String, String>, ConferenceAnalysis> kafkaStringFlowShape =
                    b.add(kafkaConvertStringFlow);

            final FlowShape<ConferenceAnalysis, ConferenceAnalysis> wordCountFlowShape = b.add(wordCountFlow);

            final FlowShape<ConferenceAnalysis, ConferenceAnalysis> sentimentAnalysisFlowShape = b.add(sentimentAnalysisFlow);

            final FlowShape<ConferenceAnalysis, List<ConferenceAnalysis>> createRankingsFlowShape = b.add(createRankingsFlow);

            b.from(kafkaSourceShape)
                    .via(kafkaStringFlowShape)
                    .via(wordCountFlowShape)
                    .via(sentimentAnalysisFlowShape)
                    .via(createRankingsFlowShape)
                    .to(s);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();

        //Done done = completableFuture.get(30, TimeUnit.SECONDS); // awaiting the future to complete with a timeout
        //wait for completition
        while(!completableFuture.isDone() && !completableFuture.isCompletedExceptionally() && !completableFuture.isCancelled() ) {
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
