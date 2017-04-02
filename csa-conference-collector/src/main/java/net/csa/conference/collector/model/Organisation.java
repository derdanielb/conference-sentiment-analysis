package net.csa.conference.collector.model;

public class Organisation extends AbstractOrganiserSponsor {

    public Organisation() {}

    public Organisation(String name) {

        super(name, "net.csa.conference.model.Organisation");
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((Organisation) obj).getName());
    }

}
