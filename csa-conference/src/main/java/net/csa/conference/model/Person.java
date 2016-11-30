package net.csa.conference.model;

/**
 * Created by mike on 30.11.16.
 */
public class Person extends Persona{
    private String firstName;

    public Person(String firstName, String name) {
        super(name);
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }
}
