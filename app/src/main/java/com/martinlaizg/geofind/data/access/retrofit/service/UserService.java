package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;

import java.io.IOException;

import retrofit2.Response;

public class UserService {

    private static UserService userService;
    private static RestClient restClient;
    private String TAG = UserService.class.getSimpleName();

    public static UserService getInstance() {
        if (restClient == null) {
            restClient = RetrofitService.getRestClient();
        }
        if (userService == null) {
            userService = new UserService();
        }
        return userService;
    }


    public User login(User user) {
        try {
            Response<User> response = restClient.login(user).execute();
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, "run: login", e);
        }
        return null;
    }

}
