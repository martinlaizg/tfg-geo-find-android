package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class UserService {

	private static UserService userService;
	private static RestClient restClient;
	private final String TAG = UserService.class.getSimpleName();

	public static UserService getInstance() {
		if(restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if(userService == null) {
			userService = new UserService();
		}
		return userService;
	}

	/**
	 * Login request
	 * If login provider is own request /login/{provider}
	 * ie: /login/own
	 * ie: /login/google
	 *
	 * @param login
	 * 		login object
	 * @return the logged user
	 * @throws APIException
	 * 		api exception
	 */
	public User login(Login login) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = restClient.login(login.getProvider().toString().toLowerCase(), login)
					.execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "login: ", e);
		}
		throw apiException;
	}

	public User registry(Login login) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = restClient.registry(login).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
		}
		throw apiException;
	}

	public boolean sendMessage(String title, String message) throws APIException {
		Response<Void> response;
		APIException apiException;
		try {
			Map<String, String> params = new HashMap<>();
			params.put("title", title);
			params.put("message", message);

			response = restClient.sendSupportMessage(params).execute();
			if(response.isSuccessful()) {
				return true;
			}
			throw ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
			throw apiException;
		}
	}

	public User update(Login login, User user, boolean isChangePassword) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = isChangePassword ?
					restClient.changeUserPassword(user.getId(), login, user).execute() :
					restClient.updateUser(user.getId(), login, user).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			throw ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
			throw apiException;
		}
	}
}
