package net.csa.conference.model;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by sky on 04.01.17.
 */
public class TimeSpanTest {

    public static TimeSpan createDefault() {
        return new TimeSpan(
                new Date(2017, 3, 7),
                new Date(2017, 3, 13)
        );
    }

    public static TimeSpan createDelay(int delay) {
        return new TimeSpan(
                new Date(2017, 3, 7),
                new Date(2017, 3, 7 + delay)
        );
    }

    @Test
    public void testEquality() {
        TimeSpan t1 = createDefault();
        TimeSpan t2 = createDefault();

        assertEquals(t1, t2);

        t2 = createDelay(5);
        assertNotEquals(t1, t2);

        t2 = new TimeSpan(
                new Date(2017, 3, 10),
                new Date(2017, 3, 13)
        );
        assertNotEquals(t1, t2);
    }
}
