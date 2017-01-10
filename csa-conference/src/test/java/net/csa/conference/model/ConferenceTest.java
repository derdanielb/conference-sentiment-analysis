package net.csa.conference.model;

import org.junit.Assert;
import org.junit.Test;

public class ConferenceTest {

	@Test
	public void thatConferenceCanBeConstructed() {
		String[] organizer = {"Person;Philipp;Amkreutz", "Group;TolleGruppe", "Organisation;TolleOrganisation"};
		String[] sponsor = {"Person;Philipp;Amkreutz", "Group;TolleGruppe", "Organisation;TolleOrganisation"};
		Conference c = new Conference("TestKonferenz", "from", "to", "TestLocationName", "Auf der Höhe", "28", "51429", "Bergisch Gladbach", "DE", "test", organizer, sponsor);
		Assert.assertTrue(c.getConferenceName() != null);
		c.initGeoLocation();
		System.out.println(c.getGeoLocation().getLatitude() + " x " + c.getGeoLocation().getLongitude());
		Assert.assertTrue(c.getGeoLocation().getLatitude() != null);
	}

}
