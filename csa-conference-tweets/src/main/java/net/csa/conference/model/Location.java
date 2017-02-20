package net.csa.conference.model;

import java.util.Objects;

/**
 * Created by Felix on 30.11.2016.
 */
public class Location {
    private double latitude;
    private double longitude;

    public Location() {}

    public Location(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(!Location.class.isAssignableFrom(obj.getClass()))
            return false;
        Location o = (Location)obj;
        return Objects.equals(latitude, o.latitude) &&
                Objects.equals(longitude, o.longitude);
    }
}
