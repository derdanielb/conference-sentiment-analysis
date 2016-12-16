package net.csa.conference.model;

import org.springframework.data.geo.Point;

/**
 * Created by leoneck on 15.12.16.
 */
public class Address {

    private String strasse;
    private int hausnummer;
    private String stadt;
    private int zipCode;
    private String land;
    private Point geoLocation;

    public String getStrasse() {
        return strasse;
    }

    public void setStrasse(String strasse) {
        this.strasse = strasse;
    }

    public int getHausnummer() {
        return hausnummer;
    }

    public void setHausnummer(int hausnummer) {
        this.hausnummer = hausnummer;
    }

    public String getStadt() {
        return stadt;
    }

    public void setStadt(String stadt) {
        this.stadt = stadt;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }

    public String getLand() {
        return land;
    }

    public void setLand(String land) {
        this.land = land;
    }

    public Point getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Point geoLocation) {
        this.geoLocation = geoLocation;
    }

    public Address() {

    }

    public Address(String strasse, int hausnummer, String stadt, int zipCode, String land, Point geoLocation) {
        this.strasse = strasse;
        this.hausnummer = hausnummer;
        this.stadt = stadt;
        this.zipCode = zipCode;
        this.land = land;
        this.geoLocation = geoLocation;
    }

}
