package com.martinlaizg.geofind.data.access.database.entity.enums;

import com.google.gson.annotations.SerializedName;

public enum PlayLevel {
    // therm', 'compass', 'any'
    @SerializedName("therm")
    THERMOMETER,
    @SerializedName("compass")
    COMPASS,
    @SerializedName("any")
    ANY

}
