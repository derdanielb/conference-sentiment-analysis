package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class GroupTest {

    public static Group createDefault() {
        return new Group("Baum");
    }

    @Test
    public void testEquality() {
        Group p1 = createDefault();
        Group p2 = createDefault();

        assertEquals(p1, p2);

        p2 = new Group("Test");
        assertNotEquals(p1, p2);
    }
}
