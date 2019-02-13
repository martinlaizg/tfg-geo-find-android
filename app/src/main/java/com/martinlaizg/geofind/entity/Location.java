package com.martinlaizg.geofind.entity;

public class Location {

    private String id;
    private String name;
    private String lat;
    private String lon;
    private String created_at;
    private String updated_at;

    public Location(String name, String lat, String lon) {
        this.name = name;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

}
