package com.devfesthackathon.devfesthackathon.app;

public class Location {
    private final double latitude;
    private final double longitude;
    private final String city;
    private final String country;

    public Location(double latitude, double longitude, String city, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.city = city;
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    @Override
    public String toString() {
        return String.format("%s, %s (%.4f, %.4f)", city, country, latitude, longitude);
    }
}
