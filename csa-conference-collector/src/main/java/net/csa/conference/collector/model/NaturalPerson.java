package net.csa.conference.collector.model;

public class NaturalPerson extends AbstractOrganiserSponsor {

    private String firstname;

    public NaturalPerson() {}

    public NaturalPerson(String name, String firstname) {
        super(name, "net.csa.conference.model.NaturalPerson");
        this.firstname = firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public boolean equals(Object obj) {
        if(this.getName().equals(((NaturalPerson) obj).getName()))
            if(this.getFirstname().equals(((NaturalPerson) obj).getFirstname())) return true;
        return false;
    }

}
