package net.csa.conference.model;

public class Group extends AbstractOrganiserSponsor {

    public Group() {}

    public Group(String name) {
        super(name);
    }

    public boolean equals(Object obj) {
        return this.getName().equals(((Group) obj).getName());
    }

}
