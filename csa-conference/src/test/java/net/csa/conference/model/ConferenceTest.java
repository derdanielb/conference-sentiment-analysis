package net.csa.conference.model;

import org.junit.Before;
import org.junit.Test;
import org.springframework.data.geo.Point;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class ConferenceTest {

    private Conference conference;

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
        conference = new Conference("0001", "dummyConference1", dummyTimePeriod1, dummyLocation1, "dummyHashtag1", dummyOrganizerList1, dummySponsorList1);

    }

    @Test
    public void getName() {
        assertEquals("dummyConference1", conference.getName());
    }

}
