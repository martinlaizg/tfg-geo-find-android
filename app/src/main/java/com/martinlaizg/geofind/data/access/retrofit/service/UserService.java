package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;
import com.martinlaizg.geofind.data.access.retrofit.error.APIError;
import com.martinlaizg.geofind.data.access.retrofit.error.ErrorUtils;

import java.io.IOException;

import retrofit2.Response;

public class UserService {

	private static UserService userService;
	private static RestClient restClient;
	private final String TAG = UserService.class.getSimpleName();

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

	public User registry(User user) {
		try {
			Response<User> response = restClient.registry(user).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			APIError apiError = ErrorUtils.parseError(response);
			User u = new User();
			u.setError(apiError);
			return u;
		} catch (IOException e) {
			Log.e(TAG, "run: login", e);
		}
		return null;
	}
}
