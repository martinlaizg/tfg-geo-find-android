package com.martinlaizg.geofind.entity.enums;

import com.google.gson.annotations.SerializedName;

public enum PlayLevel {
    // TODO: extraer a resources
    @SerializedName("therm") THERM("therm"),
    @SerializedName("compass") COMPASS("compass"),
    @SerializedName("any") ANY("any");

    private String text;


    PlayLevel(String textName) {
        this.text = textName;
    }

    public String getText() {
        return text;
    }
}
