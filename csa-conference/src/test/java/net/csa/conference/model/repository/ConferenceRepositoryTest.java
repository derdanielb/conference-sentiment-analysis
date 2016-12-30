package net.csa.conference.model.repository;

import net.csa.conference.model.Conference;
import net.csa.conference.repository.ConferenceRepository;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ConferenceRepositoryTest {

    @Autowired ConferenceRepository repository;

    @Test
    public void conferencesCanBeInserted() {
        Conference conference = new Conference("Test1");
        repository.save(conference);
    }

    @Test
    public void conferenceCanBeFound() {
        Conference conference = new Conference("Test1");
        repository.save(conference);
        List<Conference> conferences = repository.findByName("Test1");
        assert(conferences.size() == 1);
    }

}
