package com.martinlaizg.geofind.data.access.database.entity.enums;

import com.google.gson.annotations.SerializedName;

public enum PlayLevel {
    @SerializedName("any")
    ANY,
    @SerializedName("compass")
    COMPASS,
    @SerializedName("therm")
    THERMOMETER;

    public static PlayLevel getPlayLevel(int level) {
        return values()[level];
    }

}
