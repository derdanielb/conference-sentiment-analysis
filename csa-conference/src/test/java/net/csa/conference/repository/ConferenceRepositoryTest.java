package net.csa.conference.repository;

import net.csa.conference.model.*;
import static org.junit.Assert.*;

import org.assertj.core.util.IterableUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ConferenceRepositoryTest {
    @Autowired
    private ConferenceRepository repository;

    @Before
    public void clearDatabase() throws ExecutionException, InterruptedException {
        repository.deleteAll().get();
    }

    @Test
    public void testSaveSingle() {
        try {
            Conference c = createConference();
            Conference cTest = repository.save(c).get();
            assertNotNull(cTest);
            assertEquals(c, cTest);
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testCount() {
        try {
            int count = 10;
            for(int i = 0; i < count; i++)
                repository.save(createConference()).get();

            assertEquals(count, repository.count().get().longValue());
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteById() {
        try {
            Conference c = createConference();
            repository.save(c).get();
            repository.delete(c.getUuid());
            assertEquals(0, repository.count().get().longValue());
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteByConference() {
        try {
            Conference c = createConference();
            repository.save(c).get();
            repository.delete(c);
            assertEquals(0, repository.count().get().longValue());
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteMultiple() {
        try {
            List<Conference> deleters = new ArrayList<>();
            List<Conference> keepers = new ArrayList<>();
            for(int i = 0; i < 5; i++) {
                Conference c = createConference();
                repository.save(c).get();
                if(i%2 == 0)
                    deleters.add(c);
                else
                    keepers.add(c);
            }

            repository.delete(deleters).get();
            Iterable<Conference> all = repository.findAll().get();
            assertArrayEquals(keepers.toArray(), IterableUtil.toArray(all));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testDeleteAll() {
        try {
            for(int i = 0; i < 10; i++)
                repository.save(createConference()).get();

            repository.deleteAll().get();
            assertEquals(0, repository.count().get().longValue());
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    private static Conference createConference() {
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
        return c;
    }
}
