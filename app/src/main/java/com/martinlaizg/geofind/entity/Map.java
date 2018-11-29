package com.martinlaizg.geofind.entity;

import com.martinlaizg.geofind.entity.enums.PlayLevel;

public class Map {

    private String id;
    private String name;
    private String country;
    private String state;
    private String city;
    private PlayLevel min_level;
    private String created_at;
    private String updated_at;

    public Map(String id, String name, String country, String state, String city, PlayLevel min_level, String created_at, String updated_at) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.state = state;
        this.city = city;
        this.min_level = min_level;
        this.created_at = created_at;
        this.updated_at = updated_at;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public PlayLevel getMin_level() {
        return min_level;
    }

    public void setMin_level(PlayLevel min_level) {
        this.min_level = min_level;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
