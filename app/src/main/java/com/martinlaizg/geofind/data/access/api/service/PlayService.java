package com.martinlaizg.geofind.data.access.api.service;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class PlayService {

	private static final String TAG = PlayService.class.getSimpleName();
	private static PlayService instance;

	private final RestClient restClient;

	private PlayService(Application application) {
		restClient = RetrofitInstance.getRestClient(application);
	}

	public static PlayService getInstance(Application application) {
		if(instance == null) instance = new PlayService(application);
		return instance;
	}

	/**
	 * Get the user play
	 *
	 * @param user_id
	 * 		the user id
	 * @param tour_id
	 * 		the tour id
	 * @return the play
	 * @throws APIException
	 * 		exception from server
	 */
	public Play getUserPlay(int user_id, int tour_id) throws APIException {
		Response<Play> response;
		APIException apiException;
		try {
			response = restClient.getUserPlay(tour_id, user_id).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlace: ", e);
		}
		throw apiException;
	}

	/**
	 * Create the user play
	 *
	 * @param user_id
	 * 		the user id
	 * @param tour_id
	 * 		the tour id
	 * @return the play
	 * @throws APIException
	 * 		exception from server
	 */
	public Play createUserPlay(int user_id, int tour_id) throws APIException {
		Response<Play> response;
		APIException apiException;
		try {
			response = restClient.createUserPlay(tour_id, user_id).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlace: ", e);
		}
		throw apiException;
	}

	/**
	 * Create the place play
	 *
	 * @param play_id
	 * 		the play id
	 * @param place_id
	 * 		the place id
	 * @return the play
	 * @throws APIException
	 * 		exception from the server
	 */
	public Play createPlacePlay(Integer play_id, Integer place_id) throws APIException {
		Response<Play> response;
		APIException apiException;
		try {
			response = restClient.createPlacePlay(play_id, place_id).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlace: ", e);
		}
		throw apiException;
	}

	public List<Play> getUserPlays() {
		return new ArrayList<>();
	}
}
