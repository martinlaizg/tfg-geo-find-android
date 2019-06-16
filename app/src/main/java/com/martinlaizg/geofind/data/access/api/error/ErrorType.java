package com.martinlaizg.geofind.data.access.api.error;

import com.google.gson.annotations.SerializedName;

/**
 * Types of error from de server
 */
public enum ErrorType {
	@SerializedName("other") OTHER,
	@SerializedName("other") NETWORK,
	@SerializedName("username") USERNAME,
	@SerializedName("email") EMAIL,
	@SerializedName("password") PASSWORD,
	@SerializedName("id") ID,
	@SerializedName("tour_id") TOUR_ID,
	@SerializedName("user_id") USER_ID,
	@SerializedName("exist") EXIST,
	@SerializedName("token") TOKEN,
	@SerializedName("provider") PROVIDER,
	@SerializedName("provider_login") PROVIDER_LOGIN,
	@SerializedName("secure") SECURE,
	COMPLETE,}
