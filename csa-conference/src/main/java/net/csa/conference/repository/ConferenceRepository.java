package net.csa.conference.repository;

import net.csa.conference.model.Conference;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface ConferenceRepository extends PagingAndSortingRepository<Conference, Long> {

    //TODO (CRUD)

    List<Conference> findAll();

    List<Conference> findByName(String name);


}
