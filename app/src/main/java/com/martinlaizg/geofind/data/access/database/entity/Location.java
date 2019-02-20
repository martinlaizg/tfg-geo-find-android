package com.martinlaizg.geofind.data.access.database.entity;

import java.sql.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "locations")
public class Location {

    @PrimaryKey
    private int id;
    private String name;
    private String lat;
    private String lon;
    private Date created_at;
    private Date updated_at;

    public Location(int id, String name, String lat, String lon, Date created_at, Date updated_at) {
        this.id = id;
        this.name = name;
        this.lat = lat;
        this.lon = lon;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(Date updated_at) {
        this.updated_at = updated_at;
    }
}
