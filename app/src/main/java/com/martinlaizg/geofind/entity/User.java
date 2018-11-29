package com.martinlaizg.geofind.entity;

import com.martinlaizg.geofind.entity.enums.UserType;

import java.util.Date;

public class User {

    private String id;
    private String email;
    private String username;
    private Date bdate;
    private UserType user_type;
    private Date created_at;
    private Date updated_at;

    public User(String id, String email, String username, Date bdate, UserType user_type, Date created_at, Date updated_at) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.bdate = bdate;
        this.user_type = user_type;
        this.created_at = created_at;
        this.updated_at = updated_at;
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
}
