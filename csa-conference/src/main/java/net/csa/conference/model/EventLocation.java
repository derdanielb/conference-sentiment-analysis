package net.csa.conference.model;

/**
 * Created by Felix on 30.11.2016.
 */
public class EventLocation {
    private String name;
    private Address address;
    private Location geoLocation;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Location getGeoLocation() {
        return geoLocation;
    }

    public void setGeoLocation(Location geoLocation) {
        this.geoLocation = geoLocation;
    }
}
