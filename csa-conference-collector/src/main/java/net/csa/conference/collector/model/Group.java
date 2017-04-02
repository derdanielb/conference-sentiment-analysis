package net.csa.conference.collector.model;

public class Group extends AbstractOrganiserSponsor {

    public Group() {}

    public Group(String name) {

        super(name, "net.csa.conference.model.Group");
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((Group) obj).getName());
    }

}
