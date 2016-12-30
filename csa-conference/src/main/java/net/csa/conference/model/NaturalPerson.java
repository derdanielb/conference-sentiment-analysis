package net.csa.conference.model;

class NaturalPerson extends AbstractOrganiserSponsor {

    private String firstName;

    public NaturalPerson(String name, String firstName) {
        super(name);
        this.firstName = firstName;
    }
}
