package net.csa.conference.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import com.google.gson.Gson;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
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
			final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File("/Users/philippamkreutz/Documents/HBRS/OOKA/Uebungen/Uebung7/conferences.csv"))
//			final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File(args[1]))
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
						Conference conference = new Conference();
						String[] mainSplit = p.split(";");
						String conferenceMainData = mainSplit[0];
						String[] dataSplit = conferenceMainData.split(",");
						conference.setConferenceName(dataSplit[0]);
						conference.setFrom(dataSplit[1]);
						conference.setTo(dataSplit[2]);
						conference.setLocationName(dataSplit[3]);
						conference.setStreet(dataSplit[4]);
						conference.setHouseNumber(dataSplit[5]);
						conference.setPostcode(dataSplit[6]);
						conference.setCity(dataSplit[7]);
						conference.setCountry(dataSplit[8]);
						conference.setTwitterHashTag(dataSplit[9]);
						if (mainSplit.length > 1 && !mainSplit[1].equals("")) {
							List<Organizer> organizerList = new ArrayList<>();
							String organizerString = mainSplit[1];
							dataSplit = organizerString.split(",");
							List<String> dataSplitList = new LinkedList<>(Arrays.asList(dataSplit));
							while (dataSplitList.size() > 0) {
								if (dataSplitList.get(0).equals("person")) {
									organizerList.add(new Person(dataSplitList.get(1), dataSplitList.get(2)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else if (dataSplit[0].equals("group")) {
									organizerList.add(new Group(dataSplitList.get(1)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else if (dataSplit[0].equals("organisation")) {
									organizerList.add(new Organisation(dataSplitList.get(1)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else {
									dataSplitList.remove(0);
								}
							}
							conference.setOrganizerList(organizerList);
						}
						if (mainSplit.length > 2 && !mainSplit[2].equals("")) {
							List<Sponsor> sponsorList = new ArrayList<>();
							String sponsorString = mainSplit[2];
							dataSplit = sponsorString.split(",");
							List<String> dataSplitList = new LinkedList<>(Arrays.asList(dataSplit));
							while (dataSplitList.size() > 0) {
								if (dataSplitList.get(0).equals("person")) {
									sponsorList.add(new Person(dataSplitList.get(1), dataSplitList.get(2)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else if (dataSplit[0].equals("group")) {
									sponsorList.add(new Group(dataSplitList.get(1)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else if (dataSplit[0].equals("organisation")) {
									sponsorList.add(new Organisation(dataSplitList.get(1)));
									dataSplitList.remove(0);
									dataSplitList.remove(0);
								} else {
									dataSplitList.remove(0);
								}
							}
							conference.setSponsorsList(sponsorList);
						}
						return conference;
					});
			conferenceMappingFlow = flow.log("csa-conference-collector-conferenceMappingFlow");
		}

		MongoClientOptions settings = MongoClientOptions.builder().codecRegistry(MongoClient.getDefaultCodecRegistry()).build();
		MongoClient mongo = new MongoClient(new ServerAddress("localhost", 19017), settings);
		MongoDatabase db = mongo.getDatabase("ConferenceDB");
		MongoCollection conferenceDB = db.getCollection("Conferences");

		final Sink<Conference, CompletionStage<Done>> sink =
				Sink.foreach(p -> {
					Document doc = new Document();
					doc.put("confernece", new Gson().toJson(p));
					conferenceDB.insertOne(doc);
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
