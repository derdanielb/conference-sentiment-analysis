package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class LocationTest {

    public static Location createDefault() {
        Location l = new Location();
        l.setLatitude(42.42);
        l.setLongitude(42.42);
        return l;
    }

    @Test
    public void testEquality() {
        Location l1 = createDefault();
        Location l2 = createDefault();

        assertEquals(l1, l2);

        l2.setLatitude(13.13);
        assertNotEquals(l1, l2);

        l2.setLatitude(l1.getLatitude());
        l2.setLongitude(13.13);
        assertNotEquals(l1, l2);
    }
}
