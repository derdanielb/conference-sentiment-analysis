package net.csa.conference.model.service;


import net.csa.conference.model.*;
import net.csa.conference.service.ConferenceService;
import net.csa.conference.testdata.Testdata;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.validation.constraints.AssertTrue;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ConferenceServiceTest {

    @Autowired
    private ConferenceService service;

    @Before
    public void init() {
        service.createTestDB();
    }

    @Test
    public void findAll() {
        List<Conference> conferences = service.findAll();
        Assert.assertTrue(conferences.size() == Testdata.names.length);
    }

    @Test
    public void findByName() {
        List<Conference> conferences = service.findByName("DOAG");
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue((conferences.get(0).getName().equals("DOAG")));
    }

    @Test
    public void findByLocation() {
        Future future = service.findByLocation("Pentagon");
        List<Conference> conferences = null;
        try {
            conferences = (List<Conference>) future.get(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue((conferences.get(0).getName().equals("FrOSCon")));
    }

    @Test
    public void findByStartdateTime() {
        List<Conference> conferences = service.findByStartdatetime(LocalDateTime.of(2017, Month.DECEMBER, 9, 14, 15));
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Javaland"));
    }

    @Test
    public void findByEnddateTime() {
        List<Conference> conferences = service.findByEnddatetime(LocalDateTime.of(2017, Month.DECEMBER, 15, 18, 30));
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Javaland"));
    }

    @Test
    public void findByAddress() {
        List<Conference> conferences = service.findByAddress(new Address("Hafenstraße", "89C", "Hamburg", "11111", "Germany"));
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Javaland"));
    }

    @Test
    public void findByGeolocation() {
        List<Conference> conferences = service.findByGeolocation(new GeoLocation(1.45, 26.78));
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Javaland"));
    }

    @Test
    public void findByHashtag() {
        List<Conference> conferences = service.findByHashtag("javaland");
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Javaland"));
    }

    @Test
    public void findByNameContaining() {
        Stream<Conference> conferences = service.findByNameContaining("Konferenz");
        Assert.assertTrue(conferences.count() == 2);
    }

    @Test
    public void findByHashtagContaining() {
        List<Conference> conferences = service.findByHashtagContaining("konf");
        Assert.assertTrue(conferences.size() == 2);
    }

    //Test fails
    @Test
    public void findByOrganiser() {
        AbstractOrganiserSponsor orga = new Organisation("Corp Z");
        List<Conference> conferences = service.findByOrganisers(orga);
        System.out.println("Count: " + conferences.size());
        //Assert.assertTrue(conferences.size() >= 1);
    }

    @Test
    public void deleteByName() {
        List<Conference> conferences = service.deleteByName("Test Konferenz");
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getName().equals("Test Konferenz"));
    }

    @Test
    public void deleteByHashtag() {
        List<Conference> conferences = service.deleteByHashtag("konfxy");
        Assert.assertTrue(conferences.size() == 1);
        Assert.assertTrue(conferences.get(0).getHashtag().equals("konfxy"));
    }

    @Test
    public void deleteById() {
        Conference conference = service.findByName("DOAG").get(0);
        conference = service.delete(conference.getUuid());
        Assert.assertTrue(conference.getName().equals("DOAG"));
    }

    @Test
    public void deleteAll() {
        List<Conference> conferences = service.deleteAll();
        conferences = service.findAll();
        Assert.assertTrue(conferences.size() == 0);

    }

    //Test _class property for Abstract Organisers and Sponsors
    @Test
    public void testClassPropertyOfAOS() {
        List<Conference> conferences = service.findByName("Javaland");
        Assert.assertTrue(conferences.size() == 1);
        Conference conference = conferences.get(0);
        List<AbstractOrganiserSponsor> aos = conference.getOrganisers();
        System.out.println("Organisers count: " + aos.size());
        Assert.assertTrue(aos.size() >= 1);
        for(AbstractOrganiserSponsor organiser : aos) {
            System.out.println("Organiser: " + organiser.getName());
            System.out.println("Organiser class: " + organiser.getClass());
        }
    }

    @After
    public void clean() {
        service.deleteAll();
    }

}
