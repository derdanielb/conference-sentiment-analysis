package net.csa.conference.collector;

/**
 * @author philipp.amkreutz
 */
public class Organisation implements Organizer, Sponsor {

	private String name;
	private String type = "organisation";

	public Organisation() {

	}

	public Organisation(String name) {
		this.name = name;
	}

	public String getName() {
		return "Organisation: " + this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
