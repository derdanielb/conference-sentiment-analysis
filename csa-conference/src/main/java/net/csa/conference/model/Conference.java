package net.csa.conference.model;

import java.util.List;
import java.util.UUID;

public class Conference {
    private UUID uuid = UUID.randomUUID();
    private String name;
    private TimeSpan timeSpan;
    private EventLocation location;
    private String hashTag;
    private List<Persona> organisers;
    private List<Persona> sponsors;

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TimeSpan getTimeSpan() {
        return timeSpan;
    }

    public void setTimeSpan(TimeSpan timeSpan) {
        this.timeSpan = timeSpan;
    }

    public EventLocation getLocation() {
        return location;
    }

    public void setLocation(EventLocation location) {
        this.location = location;
    }

    public String getHashTag() {
        return hashTag;
    }

    public void setHashTag(String hashTag) {
        this.hashTag = hashTag;
    }

    public List<Persona> getOrganisers() {
        return organisers;
    }

    public void setOrganisers(List<Persona> organisers) {
        this.organisers = organisers;
    }

    public List<Persona> getSponsors() {
        return sponsors;
    }

    public void setSponsors(List<Persona> sponsors) {
        this.sponsors = sponsors;
    }
}
