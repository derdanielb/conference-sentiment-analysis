package net.csa.conference.model;

/**
 * @author philipp.amkreutz
 */
public class Group implements Organizer, Sponsor {

	private String name;

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return "Gruppe: " + this.name;
	}

}
