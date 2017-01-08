package net.csa.conference.model;

public class NaturalPerson extends AbstractOrganiserSponsor {

    private String firstname;

    public NaturalPerson() {}

    public NaturalPerson(String name, String firstname) {
        super(name);
        this.firstname = firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }


}
