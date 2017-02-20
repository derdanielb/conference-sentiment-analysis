package net.csa.conference;

import net.csa.conference.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestDataGenerator {
    public static Conference createConference() {
        Conference c = new Conference();
        c.generateUUID();
        c.setHashTag("baum");
        c.setName("Baum");
        List<Persona> orcs = new ArrayList<>();
        orcs.add(new Organisation("o1"));
        orcs.add(new Group("g1"));
        orcs.add(new Person("Ralf", "Baum"));
        c.setOrganisers(orcs);
        List<Persona> spon = new ArrayList<>();
        spon.add(new Organisation("o2"));
        spon.add(new Group("g2"));
        spon.add(new Person("Rolf", "B42"));
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
        c.setLocation(el);
        return c;
    }
}
