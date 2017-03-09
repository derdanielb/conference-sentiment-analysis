package net.csa.conference.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.*;
import akka.stream.javadsl.*;
import akka.util.ByteString;
import com.mongodb.*;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * @author philipp.amkreutz
 */
public class ConferenceCollector {

	private static final Logger log = LoggerFactory.getLogger(ConferenceCollector.class);

	private static Document mapConferenceStringToDocument(String conference) {
		Document document = new Document();

		String[] mainSplit = conference.split(";");
		String conferenceMainData = mainSplit[0];
		String[] dataSplit = conferenceMainData.split(",");
		document.put("conferenceName", dataSplit[0]);
		document.put("from", dataSplit[1]);
		document.put("to", dataSplit[2]);
		document.put("locationName", dataSplit[3]);
		document.put("street", dataSplit[4]);
		document.put("houseNumber", dataSplit[5]);
		document.put("postCode", dataSplit[6]);
		document.put("city", dataSplit[7]);
		document.put("country", dataSplit[8]);
		document.put("twitterHashTag", dataSplit[9]);
		if (mainSplit.length > 1 && !mainSplit[1].equals("")) {
			List<BasicDBObject> organizerList = mapConferenceListStringDBObjectList(mainSplit[1]);
			document.put("organizerList", organizerList);
		}
		if (mainSplit.length > 2 && !mainSplit[2].equals("")) {
			List<BasicDBObject> sponsorsList = mapConferenceListStringDBObjectList(mainSplit[2]);
			document.put("sponsorsList", sponsorsList);
		}
		return document;
	}

	private static List<BasicDBObject> mapConferenceListStringDBObjectList(String listString) {
		String organizerString = listString;
		String[] dataSplit = organizerString.split(",");
		List<String> dataSplitList = new LinkedList<>(Arrays.asList(dataSplit));
		List<BasicDBObject> objects = new ArrayList<>();
		while (dataSplitList.size() > 0) {
			if (dataSplitList.get(0).equals("person")) {
				BasicDBObject object = new BasicDBObject();
				object.put("firstName", dataSplitList.get(1));
				object.put("lastName", dataSplitList.get(2));
				object.put("type", "person");
				objects.add(object);
				dataSplitList.remove(0);
				dataSplitList.remove(0);
				dataSplitList.remove(0);
			} else if (dataSplitList.get(0).equals("group")) {
				BasicDBObject object = new BasicDBObject();
				object.put("name", dataSplitList.get(1));
				object.put("type", "group");
				objects.add(object);
				dataSplitList.remove(0);
				dataSplitList.remove(0);
			} else if (dataSplitList.get(0).equals("organisation")) {
				BasicDBObject object = new BasicDBObject();
				object.put("name", dataSplitList.get(1));
				object.put("type", "organisation");
				objects.add(object);
				dataSplitList.remove(0);
				dataSplitList.remove(0);
			} else {
				dataSplitList.remove(0);
			}
		}
		return objects;

	}

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

		final Flow<String, Document, NotUsed> conferenceMappingFlow;
		{
			Flow<String, Document, NotUsed> flow =
					Flow.fromFunction(p -> {
						Document document = mapConferenceStringToDocument(p);
						return document;
					});
			conferenceMappingFlow = flow.log("csa-conference-collector-conferenceMappingFlow");
		}

		MongoClient mongo = new MongoClient("localhost", 19017);
		MongoDatabase db = mongo.getDatabase("ConferenceDB");
		MongoCollection conferenceDB = db.getCollection("conference");

		final Sink<Document, CompletionStage<Done>> sink =
				Sink.foreach(p -> {
					conferenceDB.insertOne(p);
				});

		final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

			// source shape for hashtags
			final SourceShape<String> conferenceSourceShape = b.add(conferenceSource);
			final FlowShape<String, Document> conferenceMappingFlowShape = b.add(conferenceMappingFlow);

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
