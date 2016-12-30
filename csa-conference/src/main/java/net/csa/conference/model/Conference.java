package net.csa.conference.model;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Conference {

    @Id
    private long uuid;
    private String name;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private String location;
    private Address address;
    private GeoLocation geoLocation;
    private String hashtag;
    private ArrayList<AbstractOrganiserSponsor> organisers;
    private ArrayList<AbstractOrganiserSponsor> sponsors;

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
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
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

    public GeoLocation getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(GeoLocation geoLocation) {
        this.geoLocation = geoLocation;
    }

    public String getHashtag() {
        return hashtag;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public long getUuid() {
        return uuid;
    }

    public ArrayList<AbstractOrganiserSponsor> getOrganisers() {
        return organisers;
    }

    public ArrayList<AbstractOrganiserSponsor> getSponsors() {
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
