package net.csa.conference.repository;

import net.csa.conference.model.*;

import static org.hamcrest.Matchers.containsInAnyOrder;
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
import java.util.List;
import java.util.UUID;
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
    public void testSaveList() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(createConference());

            Iterable<Conference> listTest = repository.save(list).get();
            assertNotNull(listTest);
            assertArrayEquals(list.toArray(), IterableUtil.toArray(listTest));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindOne() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(repository.save(createConference()).get());

            Conference c5 = repository.findOne(list.get(5).getUuid()).get();
            assertNotNull(c5);
            assertEquals(list.get(5), c5);
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testExists() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(repository.save(createConference()).get());

            boolean c5 = repository.exists(list.get(5).getUuid()).get();
            assertNotNull(c5);
            assertTrue(c5);
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindAll() {
        try {
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++)
                list.add(repository.save(createConference()).get());

            Iterable<Conference> all = repository.findAll().get();
            assertNotNull(all);
            assertArrayEquals(list.toArray(), IterableUtil.toArray(all));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindAllList() {
        try {
            List<UUID> listUUID = new ArrayList<>();
            List<Conference> list = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Conference c = repository.save(createConference()).get();
                if(i >= 3 && i <= 6) {
                    listUUID.add(c.getUuid());
                    list.add(c);
                }
            }

            Iterable<Conference> allInList = repository.findAll(listUUID).get();
            assertNotNull(allInList);
            System.out.println(list);
            System.out.println(allInList);
            assertThat(list, containsInAnyOrder(IterableUtil.toArray(allInList)));
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
