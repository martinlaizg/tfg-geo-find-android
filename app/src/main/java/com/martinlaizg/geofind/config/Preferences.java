package com.martinlaizg.geofind.config;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;
import com.martinlaizg.geofind.utils.DateUtils;

public enum Preferences {

	USER("loggedUser");

	final String key;

	Preferences(String key) {
		this.key = key;
	}

	public static UserEntity getLoggedUser(SharedPreferences sp) {
		String user_string = sp.getString(USER.getKey(), "");

		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		return gson.fromJson(user_string, UserEntity.class);
	}

	public static void setLoggedUser(SharedPreferences sp, UserEntity userEntity) {
		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		String stringUser = userEntity.toJson();
		sp.edit().putString(USER.getKey(), stringUser).apply();
	}

	public static void logout(SharedPreferences sp) {
		sp.edit().putString(USER.getKey(), null).apply();
	}


	private String getKey() {
		return key;
	}

}
