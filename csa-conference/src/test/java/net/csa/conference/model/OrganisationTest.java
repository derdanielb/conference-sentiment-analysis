package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class OrganisationTest {

    public static Organisation createDefault() {
        return new Organisation("Baum");
    }

    @Test
    public void testEquality() {
        Organisation p1 = createDefault();
        Organisation p2 = createDefault();

        assertEquals(p1, p2);

        p2 = new Organisation("Test");
        assertNotEquals(p1, p2);
    }
}
