package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class EventLocationTest {

    public static EventLocation createDefault() {
        EventLocation e = new EventLocation();
        e.setAddress(AddressTest.createDefault());
        e.setGeoLocation(LocationTest.createDefault());
        e.setName("baum");
        return e;
    }

    @Test
    public void testEquality() {
        EventLocation e1 = createDefault();
        EventLocation e2 = createDefault();

        assertEquals(e1, e2);

        Address aMod = AddressTest.createDefault();
        aMod.setNumber(13);
        e2.setAddress(aMod);
        assertNotEquals(e1, e2);

        Location lMod = LocationTest.createDefault();
        lMod.setLatitude(13.13);
        e2.setAddress(AddressTest.createDefault());
        e2.setGeoLocation(lMod);
        assertNotEquals(e1, e2);

        e2.setGeoLocation(LocationTest.createDefault());
        e2.setName("test");
        assertNotEquals(e1, e2);
    }
}
