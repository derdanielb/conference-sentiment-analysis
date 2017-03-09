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

    public static Location fromCSVString(String csv) throws Exception {
        String[] split = csv.split(",");
        if(split.length != 2)
            throw new Exception("Location csv length must be 2");

        return new Location(Double.parseDouble(split[0].trim()), Double.parseDouble(split[1].trim()));
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
