package com.martinlaizg.geofind.dataAccess.database.entity;

import com.martinlaizg.geofind.dataAccess.database.entity.enums.PlayLevel;

import java.sql.Date;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "maps")
public class Maps {

    @PrimaryKey
    private int id;
    private String name;
    private String country;
    private String state;
    private String description;
    private String city;
    private PlayLevel min_level;
    private Date created_at;
    private Date updated_at;
    private Integer creator_id;

    public Maps(int id, String name, String country, String state, String description, String city, PlayLevel min_level, Date created_at, Date updated_at, Integer creator_id) {
        this.id = id;
        this.name = name;
        this.country = country;
        this.state = state;
        this.description = description;
        this.city = city;
        this.min_level = min_level;
        this.created_at = created_at;
        this.updated_at = updated_at;
        this.creator_id = creator_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public Integer getCreator_id() {
        return creator_id;
    }

    public void setCreator_id(Integer creator_id) {
        this.creator_id = creator_id;
    }
}
