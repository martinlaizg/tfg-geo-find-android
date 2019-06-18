package com.martinlaizg.geofind.config;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.utils.DateUtils;

public class Preferences {

	public static final String USER = "user";
	public static final String LOGIN = "login";

	public static User getLoggedUser(SharedPreferences sp) {
		String user_string = sp.getString(USER, "");
		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		return gson.fromJson(user_string, User.class);
	}

	public static void setLoggedUser(SharedPreferences sp, User user) {
		String user_string = user.toJson();
		sp.edit().putString(USER, user_string).apply();
	}

	public static void logout(SharedPreferences sp) {
		sp.edit().putString(USER, null).apply();
		sp.edit().putString(LOGIN, null).apply();
	}

	public static void setLogin(SharedPreferences sp, Login login) {
		String login_string = login.toJson();
		sp.edit().putString(LOGIN, login_string).apply();
	}

	public static Login getLogin(SharedPreferences sp) {
		String login_string = sp.getString(LOGIN, "");
		Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
		return gson.fromJson(login_string, Login.class);
	}
}
