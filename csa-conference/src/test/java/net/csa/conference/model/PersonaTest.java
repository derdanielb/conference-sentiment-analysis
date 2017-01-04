package net.csa.conference.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class PersonaTest {
    static class TestPersona extends Persona {
        public TestPersona(String name) {
            super(name);
        }
    }

    @Test
    public void testEquality() {
        Persona p1 = new TestPersona("Baum");
        Persona p2 = new TestPersona("Baum");

        assertEquals(p1, p2);

        p2 = new TestPersona("Test");
        assertNotEquals(p1, p2);
    }
}
