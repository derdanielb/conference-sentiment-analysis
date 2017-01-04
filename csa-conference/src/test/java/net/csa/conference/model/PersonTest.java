package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class PersonTest {

    public static Person createDefault() {
        return new Person("Baum", "Baumson");
    }

    @Test
    public void testEquality() {
        Person p1 = createDefault();
        Person p2 = createDefault();

        assertEquals(p1, p2);

        p2 = new Person("Baum", "Json");
        assertNotEquals(p1, p2);

        p2 = new Person("Jade", "Baumson");
        assertNotEquals(p1, p2);
    }
}
