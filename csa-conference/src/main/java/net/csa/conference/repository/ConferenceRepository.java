package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.concurrent.Future;

/**
 * @author philipp.amkreutz
 */
@Repository
@EnableAsync
public interface ConferenceRepository extends MongoRepository<Conference, String> {

	List<Conference> findByConferenceName(String conferenceName);

	@Async
	Future<List<Conference>> findByLocationName(String locationName);

	List<Conference> findByTwitterHashTag(String twitterHashTag);

	Conference findByUuid(String uuid);

}
