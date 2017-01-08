package net.csa.conference.repository;

import net.csa.conference.model.*;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.Future;

@Repository
public interface ConferenceRepository extends PagingAndSortingRepository<Conference, String> {

    //READ
    List<Conference> findAll();

    List<Conference> findByName(String name);

    @Async
    Future<List<Conference>> findByLocation(String location);

    List<Conference> findByStartdatetime(LocalDateTime startdatetime);

    List<Conference> findByEnddatetime(LocalDateTime enddatetime);

    List<Conference> findByAddress(Address address);

    List<Conference> findByGeolocation(GeoLocation geolocation);

    List<Conference> findByHashtag(String hashtag);

    List<Conference> findByOrganisers(AbstractOrganiserSponsor aos);

    List<Conference> findBySponsors(AbstractOrganiserSponsor aos);

    List<Conference> findByNameContaining(String name);

    List<Conference> findByHashtagContaining(String hashtag);

    //DELETE
    List<Conference> deleteByName(String name);

    List<Conference> deleteByHashtag(String hashtag);

}
