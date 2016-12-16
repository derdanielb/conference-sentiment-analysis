package net.csa.conference.model;

/**
 * Created by leoneck on 15.12.16.
 */
public class Location {

    private String name;
    private Address adresse;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAdresse() {
        return adresse;
    }

    public void setAdresse(Address adresse) {
        this.adresse = adresse;
    }

    public Location() {

    }

    public Location(String name, Address adresse) {
        this.name = name;
        this.adresse = adresse;
    }

}
