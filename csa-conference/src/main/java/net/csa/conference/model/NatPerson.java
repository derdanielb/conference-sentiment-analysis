package net.csa.conference.model;

/**
 * Created by leoneck on 15.12.16.
 */
public class NatPerson extends Person {

    private String vorname;

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public NatPerson() {

    }

    public NatPerson(String name, String vorname) {
        super(name);
        this.vorname = vorname;
    }

}
