package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import net.csa.conference.model.Location;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.Date;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * Created by mike on 30.11.16.
 */
public interface ConferenceRepository extends Repository<Conference, UUID> {

    @Async
    <S extends Conference> ListenableFuture<S> save(S conference);

    @Async
    ListenableFuture<Conference> findOne(UUID conferenceId);

    @Async
    ListenableFuture<Iterable<Conference>> findByNameContaining(String name);

    @Async
    ListenableFuture<Boolean> exists(UUID conferenceId);

    @Async
    ListenableFuture<Iterable<Conference>> findAll();

    @Async
    ListenableFuture<Iterable<Conference>> findAll(Iterable<UUID> conferenceIds);

    @Async
    ListenableFuture<Long> count();

    @Async
    ListenableFuture delete(UUID conferenceId);

    @Async
    ListenableFuture delete(Conference conference);

    @Async
    ListenableFuture delete(Iterable<? extends Conference> conferences);

    @Async
    ListenableFuture deleteAll();

    @Query("{" +
            "   \"location.name\": ?0" +
            "}")
    Stream<Conference> findAllByEventLocationName(String name);

    @Query("{" +
            "   \"timeSpan.begin\": {$lte: ?0}," +
            "   \"timeSpan.end\": {$gte: ?0}" +
            "}")
    Stream<Conference> findByTimeSpan(Date date);

    @Query("{" +
            "   $and: [" +
            "       { \"location.geoLocation.latitude\": { $lte: ?0}}," +
            "       { \"location.geoLocation.longitude\": { $gte: ?1}}," +
            "       { \"location.geoLocation.latitude\": { $gte: ?2}}," +
            "       { \"location.geoLocation.longitude\": { $lte: ?3}}" +
            "   ]" +
            "}")
    Stream<Conference> findByLocation(double topLeftLat, double topLeftLon, double bottomRightLat, double bottomRightLon);

    @Query("{" +
            "   $or: [" +
            "           { \"organisers.name\": ?0 }," +
            "           { \"sponsors.name\": ?0 }" +
            "        ]"+
            "}")
    Stream<Conference> findByPersonaName(String name);
}
