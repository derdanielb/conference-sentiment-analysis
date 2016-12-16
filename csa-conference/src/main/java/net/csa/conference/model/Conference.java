package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Arrays;
import java.util.List;

@Document(collection = "conference")
public class Conference {

    @Id
    private String UUID;

    private String name;
    private TimePeriod zeitraum;
    private Location veranstaltungsort;
    private String twitterHashtag;
    private List<Person> organisatoren;
    private List<Person> sponsoren;

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimePeriod getZeitraum() {
        return zeitraum;
    }

    public void setZeitraum(TimePeriod zeitraum) {
        this.zeitraum = zeitraum;
    }

    public Location getVeranstaltungsort() {
        return veranstaltungsort;
    }

    public void setVeranstaltungsort(Location veranstaltungsort) {
        this.veranstaltungsort = veranstaltungsort;
    }

    public String getTwitterHashtag() {
        return twitterHashtag;
    }

    public void setTwitterHashtag(String twitterHashtag) {
        this.twitterHashtag = twitterHashtag;
    }

    public List<Person> getOrganisatoren() {
        return organisatoren;
    }

    public void setOrganisatoren(List<Person> organisatoren) {
        this.organisatoren = organisatoren;
    }

    public List<Person> getSponsoren() {
        return sponsoren;
    }

    public void setSponsoren(List<Person> sponsoren) {
        this.sponsoren = sponsoren;
    }

    public Conference() {

    }

    public Conference(String UUID, String name, TimePeriod zeitraum, Location veranstaltungsort, String twitterHashtag, List<Person> organisatoren, List<Person> sponsoren) {
        this.UUID = UUID;
        this.name = name;
        this.zeitraum = zeitraum;
        this.veranstaltungsort = veranstaltungsort;
        this.twitterHashtag = twitterHashtag;
        this.organisatoren = organisatoren;
        this.sponsoren = sponsoren;
    }

}
