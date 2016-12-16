package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.concurrent.Future;

/**
 * Created by leoneck on 15.12.16.
 */
public interface ConferenceRepository extends PagingAndSortingRepository<Conference, String> {

    // Create: Save ist bereits vorhanden

    // Read:
    @Async Future<List<Conference>> findByName(String name);
    @Async Future<List<Conference>> findByTwitterHashtag(String twitterHastag);
    @Async Future<List<Conference>> findByVeranstaltungsortAdresseStadt(String stadt);

    // Delete:
    int deleteConferenceByName(String name);

}
