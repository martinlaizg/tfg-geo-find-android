package com.martinlaizg.geofind;

import android.util.Patterns;

public class Validator {
    public static boolean isEmail(String text) {
        return Patterns.EMAIL_ADDRESS.matcher(text).matches();
    }
}
