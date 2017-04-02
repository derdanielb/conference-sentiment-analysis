package net.csa.conference.collector;

import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.japi.function.Function;
import akka.stream.ActorMaterializer;
import akka.stream.ActorMaterializerSettings;
import akka.stream.ClosedShape;
import akka.stream.FlowShape;
import akka.stream.Graph;
import akka.stream.IOResult;
import akka.stream.Materializer;
import akka.stream.SourceShape;
import akka.stream.Supervision;
import akka.stream.javadsl.FileIO;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Framing;
import akka.stream.javadsl.FramingTruncation;
import akka.stream.javadsl.GraphDSL;
import akka.stream.javadsl.RunnableGraph;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import akka.util.ByteString;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import net.csa.conference.collector.model.Conference;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.File;
import java.util.concurrent.*;

public class ConferenceCollector {

    private static final Logger log = LoggerFactory.getLogger(ConferenceCollector.class);

    private static MongoClient mongoClient = null;
    private static MongoDatabase database = null;

    public static void main(String args[]) throws InterruptedException, TimeoutException, ExecutionException {

        if(args.length != 1) {
            log.info("Start the job with exactly one parameter for the path of the csv-file with the conferences.");
            System.exit(1);
        }

        String pathToFile = args[0];
        log.info("Path to the csv file: " + pathToFile);

        // this decider applies the default supervision strategy, but it adds some logging in case of an error
        final Function<Throwable, Supervision.Directive> decider = ex -> {
            log.error("Error during stream processing.", ex);
            return Supervision.stop();
        };

        // actor system and materializer always needed to use Akka Streams
        final ActorSystem system = ActorSystem.create();
        final Materializer materializer = ActorMaterializer.create(ActorMaterializerSettings.create(system)
                .withSupervisionStrategy(decider), system);

        // init mongoDB connection
        MongoClientURI connectionString = new MongoClientURI("mongodb://localhost:27017");
        mongoClient = new MongoClient(connectionString);
        database = mongoClient.getDatabase("conferenceDB");
        MongoCollection<Document> collection = database.getCollection("conference");

        // ----- construct the stages for the processing graph -----

        // source for conferences
        final Source<String, CompletionStage<IOResult>> conferencesSource;
        {
            // source for streaming the input file
            final Source<ByteString, CompletionStage<IOResult>> fileSource = FileIO.fromFile(new File(pathToFile))
                    .log("csa-tweet-collector-fileSource");

            // each line is used as a conference thus split by line separator
            final Flow<ByteString, String, NotUsed> framingFlow
                    = Framing.delimiter(ByteString.fromString(System.lineSeparator()), 1000, FramingTruncation.ALLOW)
                    .map(ByteString::utf8String)
                    .log("csa-tweet-collector-framingFlow");

            conferencesSource = fileSource.via(framingFlow)
                    .log("csa-tweet-collector-conferenceSource");
        }


        // flow to convert each string (line of csv file) to a conference object
        final Flow<String, Conference, NotUsed> convertStringToConferenceFlow =
                Flow.fromFunction(string -> {
                    return Conference.createFromCSV(string);
                });


        // sink to write conferences in mongoDB
        Sink<Conference, CompletionStage<Done>> sink
                = Sink.foreach(conference -> {

                    //Create bson document from conference object
                    Document doc = new Document("_class", conference.getClassProperty())
                            .append("name", conference.getName())
                            //.append("startdatetime", conference.getStartdatetime())
                            //.append("enddatetime", conference.getEnddatetime())
                            .append("location", conference.getLocation())
                            .append("address",
                                    new Document("street", conference.getAddress().getStreet())
                                            .append("houseNumber", conference.getAddress().getHouseNumber())
                                            .append("city", conference.getAddress().getCity())
                                            .append("zipCode", conference.getAddress().getZipCode())
                                            .append("country", conference.getAddress().getCountry())
                                    )
                            //.append("geolocation", conference.getGeolocation())
                            .append("hashtag", conference.getHashtag())
                            //.append("organisers", conference.getOrganisers())
                            //.append("sponsors", conference.getSponsors())
                            ;

                    collection.insertOne(doc);

            log.info("[Sink] " + conference);

        });


        // ----- construct the processing graph as required using shapes obtained for stages -----

        final Graph<ClosedShape, CompletionStage<Done>> g = GraphDSL.create(sink, (b, s) -> {

            // source shape for conferences
            final SourceShape<String> conferencesSourceShape = b.add(conferencesSource);

            // flow shape to create conference objects from string lines
            final FlowShape<String, Conference> convertStringToConferenceFlowShape = b.add(convertStringToConferenceFlow);

            b.from(conferencesSourceShape)
                    .via(convertStringToConferenceFlowShape)
                    .to(s);

            return ClosedShape.getInstance();
        });

        // run it and check the status
        CompletionStage<Done> completionStage = RunnableGraph.fromGraph(g).run(materializer);
        CompletableFuture<Done> completableFuture = completionStage.toCompletableFuture();

        Done done = completableFuture.get(60, TimeUnit.SECONDS); // awaiting the future to complete with a timeout
        //wait for completition
        //while(!completableFuture.isDone()) {
        //    Thread.sleep(100);
        //}

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

        //close mongoDB connection
        mongoClient.close();

        // trigger graceful shutdown of the actor system
        system.shutdown();
        Thread.sleep(500);
    }

}
