package com.martinlaizg.geofind.data.access.database.entity;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.access.database.entity.enums.UserType;
import com.martinlaizg.geofind.utils.DateUtils;

import java.sql.Date;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {

    @PrimaryKey
    @NonNull
    private String id;
    private String email;
    private String username;
    private String name;
    private String password;
    private Date bdate;
    private UserType user_type;
    private Date created_at;
    private Date updated_at;

    public User() {
    }

    @Ignore
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getBdate() {
        return bdate;
    }

    public void setBdate(Date bdate) {
        this.bdate = bdate;
    }

    public UserType getUser_type() {
        return user_type;
    }

    public void setUser_type(UserType user_type) {
        this.user_type = user_type;
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

    public String toJson() {
        Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
        return gson.toJson(this);
    }
}
