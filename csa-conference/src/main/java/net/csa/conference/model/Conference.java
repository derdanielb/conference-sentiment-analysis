package net.csa.conference.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Document
public class Conference {
    @Id
    private UUID uuid;
    private String name;
    private TimeSpan timeSpan;
    private EventLocation location;
    private String hashTag;
    private List<Persona> organisers;
    private List<Persona> sponsors;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public void generateUUID(){
        this.uuid = UUID.randomUUID();
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

    @Override
    public String toString() {
        return uuid.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(!Conference.class.isAssignableFrom(obj.getClass()))
            return false;
        Conference o = (Conference)obj;
        return Objects.equals(uuid, o.uuid) &&
                Objects.equals(name, o.name) &&
                Objects.equals(timeSpan, o.timeSpan) &&
                Objects.equals(location, o.location) &&
                Objects.equals(hashTag, o.hashTag) &&
                Objects.equals(organisers, o.organisers) &&
                Objects.equals(sponsors, o.sponsors);
    }
}
