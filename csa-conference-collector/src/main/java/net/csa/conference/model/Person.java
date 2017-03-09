package net.csa.conference.model;

import java.util.Objects;

/**
 * Created by mike on 30.11.16.
 */
public class Person extends Persona{
    private String firstName;

    public Person(String firstName, String name) {
        super(name);
        this.firstName = firstName;
    }
    public Person() {}

    public String getFirstName() {
        return firstName;
    }

    @Override
    public boolean equals(Object obj) {
        if(!super.equals(obj))
            return false;
        if(!Person.class.isAssignableFrom(obj.getClass()))
            return false;
        Person o = (Person)obj;
        return Objects.equals(firstName, o.firstName);
    }
}
