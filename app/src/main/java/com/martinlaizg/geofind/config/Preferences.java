package com.martinlaizg.geofind.config;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    public static final String LOGGED = "logged";
    public static final String USER = "loggedUser";

    private static final String APP_KEY = "MiApp";
    private String key;

    Preferences(String key) {
        this.key = key;
    }

    public static SharedPreferences getInstance(Context applicationContext) {
        return applicationContext.getSharedPreferences(APP_KEY, Context.MODE_PRIVATE);
    }

    public String getKey() {
        return key;
    }
}
