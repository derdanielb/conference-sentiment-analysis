package net.csa.conference.model;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ConferenceTest {

	@Test
	public void thatConferenceCanBeConstructed() {
		Person person = new Person("Philipp", "Amkreutz");
		Group group = new Group("TolleGruppe");
		Organisation organisation = new Organisation("TolleOrganisation");
		List<Organizer> organizerList = new ArrayList<>();
		organizerList.add(person);
		organizerList.add(group);
		organizerList.add(organisation);
		List<Sponsor> sponsorList = new ArrayList<>();
		sponsorList.add(person);
		sponsorList.add(group);
		sponsorList.add(organisation);
		Conference c = new Conference("TestKonferenz", "from", "to", "TestLocationName", "Auf der Höhe", "28", "51429", "Bergisch Gladbach", "DE", "test", organizerList, sponsorList);
		Assert.assertTrue(c.getConferenceName() != null);
		c.initGeoLocation();
		System.out.println(c.getGeoLocation().getLatitude() + " x " + c.getGeoLocation().getLongitude());
		Assert.assertTrue(c.getGeoLocation().getLatitude() != null);
	}

}
