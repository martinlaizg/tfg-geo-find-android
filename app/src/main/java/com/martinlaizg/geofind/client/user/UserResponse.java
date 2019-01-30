package com.martinlaizg.geofind.client.user;

public class UserResponse {
    private String id;
    private String email;
    private String username;
    private String name;
    private String bdate;
    private String user_type;
    private String created_at;
    private String updated_at;

    public UserResponse(String id, String email, String username, String name, String bdate, String user_type, String created_at, String updated_at) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBdate() {
        return bdate;
    }

    public void setBdate(String bdate) {
        this.bdate = bdate;
    }

    public String getUser_type() {
        return user_type;
    }

    public void setUser_type(String user_type) {
        this.user_type = user_type;
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
