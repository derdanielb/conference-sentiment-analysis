package net.csa.conference.model;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ConferenceTest {

    private static List<Persona> createPersonas() {
        List<Persona> l = new ArrayList<>();
        l.add(PersonTest.createDefault());
        l.add(GroupTest.createDefault());
        l.add(OrganisationTest.createDefault());
        return l;
    }

    public static Conference createDefault() {
        Conference c = new Conference();
        c.generateUUID();
        c.setName("Baum");
        c.setHashTag("baum");
        c.setLocation(EventLocationTest.createDefault());
        c.setOrganisers(createPersonas());
        c.setSponsors(createPersonas());
        c.setTimeSpan(TimeSpanTest.createDefault());
        return c;
    }

    @Test
    public void testEquality() {
        Conference c1 = createDefault();
        Conference c2 = createDefault();
        c2.setUuid(c1.getUuid());

        assertEquals(c1, c2);

        c2.generateUUID();
        assertNotEquals(c1, c2);

        c2.setUuid(c1.getUuid());
        c2.setName("Test");
        assertNotEquals(c1, c2);

        c2.setName(c1.getName());
        c2.setHashTag("test");
        assertNotEquals(c1, c2);

        c2.setHashTag(c1.getHashTag());
        c2.getLocation().setName("test");
        assertNotEquals(c1, c2);

        c2.setLocation(EventLocationTest.createDefault());
        c2.getOrganisers().remove(0);
        assertNotEquals(c1, c2);

        c2.setOrganisers(createPersonas());
        c2.getSponsors().remove(0);
        assertNotEquals(c1, c2);

        c2.setSponsors(createPersonas());
        c2.setTimeSpan(TimeSpanTest.createDelay(4));
        assertNotEquals(c1, c2);
    }

}
