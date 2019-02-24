package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;
import com.martinlaizg.geofind.data.access.retrofit.error.APIError;
import com.martinlaizg.geofind.data.access.retrofit.error.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    public List<User> getAllUsers() {
        Response<List<User>> response = null;
        try {
            response = restClient.getUsers(new HashMap<>()).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            APIError apiError = ErrorUtils.parseError(response);
        } catch (IOException ex) {
            Log.e(TAG, "getAllUsers: ", ex);
        }
        return new ArrayList<>();
    }

    public User getSingle(int id) {
        // TODO: unimplemented method
        return null;
    }

    public void insert(User user) {
        // TODO: unimplemented method
    }

    public void update(User user) {
        // TODO: unimplemented method
    }

    public void deleteAllUsers() {
        // TODO: unimplemented method

    }

    public User login(User user) {
        Response<User> response = null;
        try {
            response = restClient.login(user).execute();
            if (response.isSuccessful()) {
                return response.body();
            }
        } catch (IOException ex) {
            Log.e(TAG, "getAllUsers: ", ex);
        }
        return null;
    }
}
