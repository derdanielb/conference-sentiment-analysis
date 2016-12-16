package net.csa.conference.repository;

import net.csa.conference.model.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.Point;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import static org.junit.Assert.*;

/**
 * Created by leoneck on 15.12.16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootApplication
@TestPropertySource(locations = "classpath:application.properties")
public class ConferenceRepositoryTest {

    @Autowired
    private ConferenceRepository repository;

    @Before
    public void setUp() throws Exception {
        NatPerson dummyNatPerson1 = new NatPerson("Mustermann", "Max");
        ArrayList<Person> dummyOrganizerList1 = new ArrayList<>();
        dummyOrganizerList1.add(dummyNatPerson1);
        Organisation dummyOrganisation1 = new Organisation("Mustergruppe");
        ArrayList<Person> dummySponsorList1 = new ArrayList<>();
        dummySponsorList1.add(dummyOrganisation1);
        Point dummyGeoLocation1 = new Point(10.12, 12.10);
        Address dummyAddress1 = new Address("dummyStrasse", 1, "Sankt Augustin", 53757, "Deutschland", dummyGeoLocation1);
        Location dummyLocation1 = new Location("dummyLocation1", dummyAddress1);
        TimePeriod dummyTimePeriod1 = new TimePeriod("01.12.2016", "12.12.2016");
        Conference dummyConference1 = new Conference("0001", "dummyConference1", dummyTimePeriod1, dummyLocation1, "dummyHashtag1", dummyOrganizerList1, dummySponsorList1);

        repository.save(dummyConference1);
    }

    @After
    public void tearDown() throws Exception {
        repository.deleteAll();
    }

    @Test
    public void findByName() throws Exception {
        Future<List<Conference>> confFuture = repository.findByName("dummyConference1");

        Conference conf = confFuture.get().get(0);

        assertEquals("dummyConference1", conf.getName());
        assertEquals("Sankt Augustin", conf.getVeranstaltungsort().getAdresse().getStadt());
    }

    @Test
    public void findByTwitterHashtag() throws Exception {
        Future<List<Conference>> confFuture = repository.findByTwitterHashtag("dummyHashtag1");

        Conference conf = confFuture.get().get(0);

        assertEquals("dummyConference1", conf.getName());
        assertEquals("dummyHashtag1", conf.getTwitterHashtag());
        assertEquals("Sankt Augustin", conf.getVeranstaltungsort().getAdresse().getStadt());
    }

    @Test
    public void findByVeranstaltungsortAdresseStadt() throws Exception {
        Future<List<Conference>> confFuture = repository.findByVeranstaltungsortAdresseStadt("Sankt Augustin");

        List<Conference> conf = confFuture.get();

        assertEquals("dummyConference1", conf.get(0).getName());
        assertEquals("Sankt Augustin", conf.get(0).getVeranstaltungsort().getAdresse().getStadt());
    }

    @Test
    public void deleteConferenceByName() throws Exception {
        repository.deleteConferenceByName("dummyConference1");

        assertEquals(0, repository.findByName("dummyConference1").get().size());
    }

}