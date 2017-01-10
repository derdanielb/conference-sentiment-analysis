package net.csa.conference.model;

/**
 * @author philipp.amkreutz
 */
public class Organisation implements Organizer, Sponsor {

	private String name;

	public Organisation(String name) {
		this.name = name;
	}

	public String getName() {
		return "Organisation: " + this.name;
	}

}
