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
	 * If login provider is own request to /login
	 * Otherwise /login/{provider}
	 * ie: /login/google
	 *
	 * @param login login object
	 * @return the logged user
	 * @throws APIException api exception
	 */
	public User login(Login login) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			if(login.getProvider() == Login.Provider.OWN) {
				response = restClient.login(login).execute();
			} else {
				response = restClient
						.loginProvider(login.getProvider().toString().toLowerCase(), login)
						.execute();
			}
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "loginProvider: ", e);
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

	@SuppressWarnings("SameReturnValue")
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
}
