package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.Future;
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

    @Query("{ eventLocation: { name: ?0} }")
    Stream<Conference> findAllByEventLocationNameContaining(String name);
}
