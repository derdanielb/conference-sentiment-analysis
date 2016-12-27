package net.csa.conference;

import net.csa.conference.model.*;
import net.csa.conference.repository.ConferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.util.ArrayList;

@SpringBootApplication
class ConferenceMicroservice implements CommandLineRunner{

    @Autowired
    private ConferenceRepository repository;

    public static void main(String[] args) {
        SpringApplication.run(ConferenceMicroservice.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        createTestData();

    }

    public void createTestData() {
        repository.deleteAll();

        NatPerson dummyNatPerson1 = new NatPerson("Mustermann", "Max");
        ArrayList<Person> dummyOrganizerList1 = new ArrayList<>();
        dummyOrganizerList1.add(dummyNatPerson1);
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

        Conference dummyConference2 = new Conference("0002", "dummyConference2", dummyTimePeriod1, dummyLocation1, "dummyHashtag2", dummyOrganizerList1, dummySponsorList1);
        repository.save(dummyConference2);
    }

}
