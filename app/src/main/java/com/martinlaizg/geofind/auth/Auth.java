package com.martinlaizg.geofind.auth;

import com.martinlaizg.geofind.Validator;

public class Auth {

    private static String AUTH_EMAIL = "martin@martin.com";
    private static String AUTH_PASSWORD = "martin";


    public static boolean login(String email, String password) {
        return Validator.isEmail(email) && AUTH_EMAIL.equals(email) && AUTH_PASSWORD.equals(password);
    }
}
