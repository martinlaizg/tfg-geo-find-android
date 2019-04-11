package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.io.IOException;

import retrofit2.Response;

public class UserService {

	private static UserService userService;
	private static RestClient restClient;
	private final String TAG = UserService.class.getSimpleName();

	public static UserService getInstance() {
		if (restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if (userService == null) {
			userService = new UserService();
		}
		return userService;
	}

	public User login(User user) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = restClient.login(user).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "login: ", e);
		}
		throw apiException;
	}

	public User registry(User user) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = restClient.registry(user).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
		}
		throw apiException;
	}
}
