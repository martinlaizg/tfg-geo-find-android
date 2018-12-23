package com.martinlaizg.geofind.client.login;

import com.martinlaizg.geofind.Validator;
import com.martinlaizg.geofind.entity.User;

public class LoginRequest {

    private User user;

    public LoginRequest(String email, String password) {
        user = new User(email, password);
    }


    public boolean validate() {
        String email = user.getEmail();
        String password = user.getPassword();
        return Validator.isEmail(email) && !email.isEmpty() && !password.isEmpty();
    }
}
