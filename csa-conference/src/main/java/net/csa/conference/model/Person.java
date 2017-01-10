package net.csa.conference.model;

/**
 * @author philipp.amkreutz
 */
public class Person implements Organizer, Sponsor {

	private String firstName;
	private String lastName;

	public Person(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public String getName() {
		return "Person: " + firstName + " " + lastName;
	}

}
