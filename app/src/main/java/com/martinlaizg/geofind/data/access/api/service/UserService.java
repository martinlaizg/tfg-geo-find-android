package com.martinlaizg.geofind.data.access.api.service;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;

public class UserService {

	private static final String TAG = UserService.class.getSimpleName();

	private static UserService instance;
	private static RestClient restClient;

	private UserService(Application application) {
		restClient = RetrofitInstance.getRestClient(application);
	}

	public static UserService getInstance(Application application) {
		if(instance == null) instance = new UserService(application);
		return instance;
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
			response = restClient.login(login).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "login: ", e);
		}
		throw apiException;
	}

	/**
	 * Registry the user
	 *
	 * @param login
	 * 		the login data
	 * @return the registred user
	 * @throws APIException
	 * 		exception from server
	 */
	public User registry(Login login) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			response = restClient.registry(login).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
		}
		throw apiException;
	}

	/**
	 * Send message to support
	 *
	 * @param title
	 * 		the title
	 * @param message
	 * 		the message
	 * @throws APIException
	 * 		exception from server
	 */
	public void sendMessage(String title, String message) throws APIException {
		Response<Void> response;
		APIException apiException;
		try {
			Map<String, String> params = new HashMap<>();
			params.put("title", title);
			params.put("message", message);

			response = restClient.sendSupportMessage(params).execute();
			if(response.isSuccessful()) {
				return;
			}
			throw ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
			throw apiException;
		}
	}

	/**
	 * Upadte user data
	 *
	 * @param login
	 * 		the login data
	 * @param user
	 * 		the user data
	 * @return the new user
	 * @throws APIException
	 * 		exception from server
	 */
	public User update(Login login, User user) throws APIException {
		Response<User> response;
		APIException apiException;
		try {
			login.setUser(user);
			response = restClient.updateUser(user.getId(), login).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			throw ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "registry: ", e);
			throw apiException;
		}
	}
}
