package com.martinlaizg.geofind.entity.enums;

public enum UserType {

    ADMIN("Administrador"),
    CREATOR("Creador"),
    USER("Usuario");

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
