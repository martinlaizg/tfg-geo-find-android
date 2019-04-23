package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.io.IOException;

import retrofit2.Response;

public class PlayService {

	private static PlayService locationService;
	private static RestClient restClient;
	private final String TAG = PlayService.class.getSimpleName();

	public static PlayService getInstance() {
		if (restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if (locationService == null) {
			locationService = new PlayService();
		}
		return locationService;
	}

	public Play getUserPlay(int user_id, int tour_id) throws APIException {
		Response<Play> response;
		APIException apiException;
		try {
			response = restClient.getUserPlay(user_id, tour_id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlace: ", e);
		}
		throw apiException;
	}

	public Play createUserPlay(int user_id, int tour_id) throws APIException {
		Response<Play> response;
		APIException apiException;
		try {
			response = restClient.createUserPlay(user_id, tour_id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlace: ", e);
		}
		throw apiException;
	}
}
