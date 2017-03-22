package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Konferenz {
    @Id
    private String uuid;
    private String konferenz_name;
    private Twitterhashtag twitterhash;
    private Integer zeitinterval;
    private Veranstaltungsort ort;
    private GeoLocation geolocation;
    private Sponsor sponsor;
    private Person person;
    private Organisator organisator;

    public Konferenz(String uuid, String konferenz_name, Integer zeitinterval, Veranstaltungsort ort, GeoLocation geolocation, Sponsor sponsor, Person person, Twitterhashtag twitterhash, Organisator organisator){
        this.uuid = uuid;
        this.konferenz_name = konferenz_name;
        this.zeitinterval = zeitinterval;
        this.ort = ort;
        this.geolocation = geolocation;
        this.sponsor = sponsor;
        this.person = person;
        this.twitterhash = twitterhash;
        this.organisator = organisator;
    }



    public Veranstaltungsort getOrt() {
        return ort;
    }

    public void setOrt(Veranstaltungsort ort) {
        this.ort = ort;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uUID) {
        uuid = uUID;
    }

    public String getKonferenz_name() {
        return konferenz_name;
    }

    public void setKonferenz_name(String konferenz_name) {
        this.konferenz_name = konferenz_name;
    }

    public Integer getZeitinterval() {
        return zeitinterval;
    }

    public void setZeitinterval(Integer zeitinterval) {
        this.zeitinterval = zeitinterval;
    }

    public String formatstring() {
        String returnstring = uuid + " " + konferenz_name + " " + zeitinterval;
        return returnstring;
    }

    public GeoLocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeoLocation geolocation) {
        this.geolocation = geolocation;
    }

    public Sponsor getSponsor() {
        return sponsor;
    }

    public void setSponsor(Sponsor sponsor) {
        this.sponsor = sponsor;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public Twitterhashtag getTwitterhash() {
        return twitterhash;
    }

    public void setTwitterhash(Twitterhashtag twitterhash) {
        this.twitterhash = twitterhash;
    }
}
