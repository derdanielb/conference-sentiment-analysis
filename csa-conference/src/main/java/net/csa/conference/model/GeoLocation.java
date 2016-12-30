package net.csa.conference.model;

class GeoLocation {

    private double latitude;
    private double longitude;

    public GeoLocation(double theLatitude, double theLongitude) {
        latitude = theLatitude;
        longitude = theLongitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double theLatitude) {
        this.latitude = theLatitude;
    }

    public void setLongitude(double theLongitude) {
        this.longitude = theLongitude;
    }

    public String toString() {
        return "latitude: " + latitude + ", longitude: " + longitude;
    }

}


