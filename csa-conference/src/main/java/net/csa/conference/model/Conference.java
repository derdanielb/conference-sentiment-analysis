package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Document
public class Conference {

    @Id
    private String uuid;
    private String name;
    private LocalDateTime startdatetime;
    private LocalDateTime enddatetime;
    private String location;
    private Address address;
    private GeoLocation geolocation;
    private String hashtag;
    private List<AbstractOrganiserSponsor> organisers = new ArrayList<>();
    private List<AbstractOrganiserSponsor> sponsors = new ArrayList<>();

    public Conference() {}

    public Conference(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDateTime getStartDateTime() {
        return startdatetime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startdatetime = startDateTime;
    }

    public LocalDateTime getEnddatetime() {
        return enddatetime;
    }

    public void setEnddatetime(LocalDateTime enddatetime) {
        this.enddatetime = enddatetime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public GeoLocation getGeolocation() {
        return geolocation;
    }

    public void setGeolocation(GeoLocation geolocation) {
        this.geolocation = geolocation;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getUuid() {
        return uuid;
    }

    public List<AbstractOrganiserSponsor> getOrganisers() {
        return organisers;
    }

    public List<AbstractOrganiserSponsor> getSponsors() {
        return sponsors;
    }

    public void addOrganiser(AbstractOrganiserSponsor aos) {
        organisers.add(aos);
    }

    public void addSponsor(AbstractOrganiserSponsor aos) {
        sponsors.add(aos);
    }

    public void removeOrganiser(AbstractOrganiserSponsor aos) {
        organisers.remove(aos);
    }

    public void removeSponsor(AbstractOrganiserSponsor aos) {
        sponsors.remove(aos);
    }

}
