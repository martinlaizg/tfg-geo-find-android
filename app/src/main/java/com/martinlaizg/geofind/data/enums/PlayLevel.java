package com.martinlaizg.geofind.data.enums;

import com.google.gson.annotations.SerializedName;

public enum PlayLevel {
	@SerializedName("tourEntity") MAP,
	@SerializedName("compass") COMPASS,
	@SerializedName("therm") THERMOMETER;

	public static PlayLevel getPlayLevel(int level) {
		return values()[level];
	}

}
