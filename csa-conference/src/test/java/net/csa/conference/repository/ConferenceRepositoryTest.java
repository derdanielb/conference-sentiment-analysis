package net.csa.conference.repository;

import net.csa.conference.model.*;

import static net.csa.conference.TestDataGenerator.createConference;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

import org.assertj.core.util.IterableUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
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
            assertThat(list, containsInAnyOrder(IterableUtil.toArray(allInList)));
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
            assertNotNull(all);
            assertThat(keepers, containsInAnyOrder(IterableUtil.toArray(all)));
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

    @Test
    public void testFindByNameContaining() {
        try {
            Conference c1 = createConference();
            c1.setName("Baum42");
            repository.save(c1).get();
            Conference c2 = createConference();
            c2.setName("Baum666");
            repository.save(c2).get();

            Iterable<Conference> list = repository.findByNameContaining("42").get();
            assertThat(Arrays.asList(c1), containsInAnyOrder(IterableUtil.toArray(list)));

            list = repository.findByNameContaining("666").get();
            assertThat(Arrays.asList(c2), containsInAnyOrder(IterableUtil.toArray(list)));

            list = repository.findByNameContaining("Baum").get();
            assertThat(Arrays.asList(c1, c2), containsInAnyOrder(IterableUtil.toArray(list)));
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindAllByEventLocationNameContaining() {
        try {
            Conference c1 = createConference();
            c1.getLocation().setName("Baum");
            repository.save(c1).get();
            Conference c2 = createConference();
            c2.getLocation().setName("42");
            repository.save(c2).get();
            Conference c3 = createConference();
            c3.getLocation().setName("Baum42");
            repository.save(c3).get();

            try (Stream<Conference> stream = repository.findAllByEventLocationNameContaining("Baum")) {
                stream.forEach(conference -> assertThat(Arrays.asList(c1, c3), contains(conference)));
            }
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    @Test
    public void testFindByTimeSpan() {
        try {
            Conference c1 = createConference();
            c1.setTimeSpan(new TimeSpan(new Date(2017, 1, 1), new Date(2017, 1, 3)));
            repository.save(c1).get();
            Conference c2 = createConference();
            c2.setTimeSpan(new TimeSpan(new Date(2017, 2, 1), new Date(2017, 2, 3)));
            repository.save(c2).get();
            Conference c3 = createConference();
            c3.setTimeSpan(new TimeSpan(new Date(2017, 3, 1), new Date(2017, 3, 3)));
            repository.save(c3).get();

            try (Stream<Conference> stream = repository.findByTimeSpan(new Date(2017, 2, 2))) {
                stream.forEach(conference -> {
                    assertEquals(c2, conference);
                });
            }
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }

    public void testFindByPersonaName() {
        try {
            Conference c1 = createConference();
            c1.getSponsors().add(new Group("OrgaMaster"));
            repository.save(c1).get();
            Conference c2 = createConference();
            c1.getOrganisers().add(new Group("OrgaMaster"));
            repository.save(c2).get();
            Conference c3 = createConference();
            repository.save(c3).get();

            try (Stream<Conference> stream = repository.findByPersonaName("OrgaMaster")) {
                stream.forEach(conference -> assertThat(Arrays.asList(c1, c2), contains(conference)));
            }
        } catch (InterruptedException | ExecutionException e) {
            fail(e.getMessage());
        }
    }
}
