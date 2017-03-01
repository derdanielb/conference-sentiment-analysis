package net.csa.conference.collector;

/**
 * @author philipp.amkreutz
 */
public class Person implements Organizer, Sponsor {

	private String firstName;
	private String lastName;
	private String type = "person";

	public Person() {
	}

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getName() {
		return "Person: " + firstName + " " + lastName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
