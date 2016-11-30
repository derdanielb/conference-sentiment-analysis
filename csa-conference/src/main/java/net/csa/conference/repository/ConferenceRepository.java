package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

/**
 * Created by mike on 30.11.16.
 */
public interface ConferenceRepository extends MongoRepository<Conference, UUID> {

}
