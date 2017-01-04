package net.csa.conference.model;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by sky on 04.01.17.
 */
public class AddressTest {

    public static Address createDefault() {
        Address a = new Address();
        a.setCountry("Germany");
        a.setNumber(42);
        a.setStreet("Baum Street");
        a.setTown("Baum City");
        a.setZipCode(42);
        return a;
    }

    @Test
    public void testEquality() {
        Address a1 = createDefault();
        Address a2 = createDefault();

        assertEquals(a1, a2);

        a2.setCountry("G2");
        assertNotEquals(a1, a2);

        a2.setCountry(a1.getCountry());
        a2.setNumber(13);
        assertNotEquals(a1, a2);

        a2.setNumber(a1.getNumber());
        a2.setStreet("S2");
        assertNotEquals(a1, a2);

        a2.setStreet(a1.getStreet());
        a2.setTown("T2");
        assertNotEquals(a1, a2);

        a2.setTown(a1.getTown());
        a2.setZipCode(13);
        assertNotEquals(a1, a2);
    }
}
