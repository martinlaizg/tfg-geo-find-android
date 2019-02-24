package com.martinlaizg.geofind.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.utils.DateUtils;

public class Preferences {

    public static final String LOGGED = "logged";
    public static final String USER = "loggedUser";
    private static final String APP_KEY = "MiApp";


    private static SharedPreferences sp;

    private String key;

    public static User getLoggedUser(Context context) {
        initializeSharedPreferences(context);

        String user_string = sp.getString(USER, "");

        Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
        return gson.fromJson(user_string, User.class);
    }

    private static void initializeSharedPreferences(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
        }
    }

    public static SharedPreferences getInstance(Context applicationContext) {
        initializeSharedPreferences(applicationContext);
        return sp;
    }

    public static void setLoggedUser(Context context, User user) {
        initializeSharedPreferences(context);
        Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
        String stringUser = gson.toJson(user);
        sp.edit().putString(USER, stringUser).apply();
    }
}
