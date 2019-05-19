package com.martinlaizg.geofind.data.access.api.error;

import com.google.gson.annotations.SerializedName;

/**
 * Types of error from de server
 */
public enum ErrorType {
	@SerializedName("other") OTHER,
	@SerializedName("other") NETWORK,
	@SerializedName("email") EMAIL,
	@SerializedName("id") ID,
	@SerializedName("tour_id") TOUR_ID,
	@SerializedName("user_id") USER_ID,
	@SerializedName("exist") EXIST,
	@SerializedName("completed") COMPLETED,
	@SerializedName("username") USERNAME}
