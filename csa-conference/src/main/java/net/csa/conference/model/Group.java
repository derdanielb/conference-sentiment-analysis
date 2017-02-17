package net.csa.conference.model;

/**
 * @author philipp.amkreutz
 */
public class Group implements Organizer, Sponsor {

	private String name;
	private String type = "group";

	public Group() {

	}

	public Group(String name) {
		this.name = name;
	}

	public String getName() {
		return "Gruppe: " + this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
