package net.csa.conference.repository;

import net.csa.conference.model.*;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConferenceRepositoryTest {
    @Autowired ConferenceRepository repository;

    @Test
    public void conferenceTest() {
        Conference c = new Conference();
        c.setHashTag("baum");
        c.setName("Baum");
        List<Persona> orcs = new ArrayList<>();
        orcs.add(new Organisation("o1"));
        orcs.add(new Group("g1"));
        orcs.add(new Person("Ralf", "Baum"));
        c.setOrganisers(orcs);
        List<Persona> spon = new ArrayList<>();
        orcs.add(new Organisation("o2"));
        orcs.add(new Group("g2"));
        orcs.add(new Person("Rolf", "B42"));
        c.setSponsors(spon);
        c.setTimeSpan(new TimeSpan(new Date(2016, 1, 1),
                new Date(2016, 1, 2)));
        EventLocation el = new EventLocation();
        el.setName("Loc");
        el.setGeoLocation(new Location(42, 42));
        Address a = new Address();
        a.setCountry("DE");
        a.setNumber(42);
        a.setStreet("BaumStreet");
        a.setTown("BaumCity");
        a.setZipCode(424242);
        el.setAddress(a);

        repository.insert(c);

        Conference cTest = repository.findOne(c.getUuid());
        assertNotNull(cTest);
        assertEquals(c, cTest);
    }
}
