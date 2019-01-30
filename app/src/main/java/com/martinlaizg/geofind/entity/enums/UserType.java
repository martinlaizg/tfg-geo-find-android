package com.martinlaizg.geofind.entity.enums;

import com.google.gson.annotations.SerializedName;

public enum UserType {

    @SerializedName("admin") ADMIN("Administrador"),
    @SerializedName("creator") CREATOR("Creador"),
    @SerializedName("user") USER("Usuario");

    private String title;

    UserType(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
