package net.csa.conference.model.model;

import net.csa.conference.model.Address;
import net.csa.conference.model.Conference;
import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;

public class ConferenceTest {

    @Test
    public void thatConferenceCanBeConstructed() {

        Conference conference = new Conference("TestConference");
        conference.setLocation("Building A");
        conference.setHashtag("#test");
        conference.setAddress(new Address("Street", "23A", "New York", "66666", "USA"));
        conference.setStartdatetime(LocalDateTime.of(2017, Month.APRIL, 10, 20, 30));
        conference.setEnddatetime(LocalDateTime.of(2017, Month.APRIL, 12, 15, 00));
        double[] geolocation = {-20.5, 34.7};
        conference.setGeolocation(geolocation);

        Assert.assertEquals(conference.getName(), "TestConference");
    }

}
