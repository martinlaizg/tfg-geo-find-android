package com.martinlaizg.geofind.config;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.utils.DateUtils;

public class Preferences {

	private static final String USER = "loggedUser";

	public static User getLoggedUser(SharedPreferences sp) {
		String user_string = sp.getString(USER, "");

		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		return gson.fromJson(user_string, User.class);
	}

	public static void setLoggedUser(SharedPreferences sp, User user) {
		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		String stringUser = user.toJson();
		sp.edit().putString(USER, stringUser).apply();
	}

	public static void logout(SharedPreferences sp) {
		sp.edit().putString(USER, null).apply();
	}

}
