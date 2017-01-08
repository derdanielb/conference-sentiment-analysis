package net.csa.conference.model.repository;

import net.csa.conference.model.Address;
import net.csa.conference.model.Conference;
import net.csa.conference.model.GeoLocation;
import net.csa.conference.repository.ConferenceRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ConferenceRepositoryTest {

    @Autowired
    private ConferenceRepository repository;


    @Before
    public void init() {
        repository.deleteAll();
    }

    @Test
    public void conferencesCanBeInserted() {
        Conference conference = new Conference("Test");
        conference.setLocation("Building A");
        conference.setHashtag("#test");
        conference.setAddress(new Address("Street", "23A", "New York", "66666", "USA"));
        conference.setStartDateTime(LocalDateTime.of(2017, Month.APRIL, 10, 20, 30));
        conference.setEnddatetime(LocalDateTime.of(2017, Month.APRIL, 12, 15, 00));
        conference.setGeolocation(new GeoLocation(-20.5, 34.7));

        conference = repository.save(conference);

        Assert.assertTrue(conference != null);
    }

    @Test
    public void conferenceCanBeUpdated() {
        Conference conference = repository.save(new Conference("Test"));
        List<Conference> conferences = repository.findByName("Test");

        conference = conferences.get(0);
        conference.setHashtag("#test");
        conference = repository.save(conference);
        Assert.assertTrue(conference != null);
        conferences = repository.findByName("Test");
        conference = conferences.get(0);
        Assert.assertTrue(conference.getHashtag().equals("#test"));
    }

    @Test
    public void conferenceCanBeFoundByName() {
        Conference conference = repository.save(new Conference("Test"));
        List<Conference> conferences = repository.findByName("Test");
        Assert.assertTrue(!conferences.isEmpty());
    }

    @Test
    public void conferenceRepositoryFutureTest() {
        Conference conference = new Conference("Test");
        conference.setLocation("TestLocation");
        conference = repository.save(conference);
        Future<List<Conference>> future = repository.findByLocation("TestLocation");
        List<Conference> conferences = null;
        try {
            conferences = future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        conference = conferences.get(0);
        Assert.assertTrue(conference.getLocation().equals("TestLocation"));
    }

    @After
    public void clean() {
        repository.deleteAll();
    }


}
