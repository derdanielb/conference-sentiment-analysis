package net.csa.conference.model;

public class Organisation extends AbstractOrganiserSponsor {

    public Organisation() {}

    public Organisation(String name) {
        super(name);
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((Organisation) obj).getName());
    }

}
