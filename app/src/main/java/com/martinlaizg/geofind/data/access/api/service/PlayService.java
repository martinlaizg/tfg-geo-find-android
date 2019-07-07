package com.martinlaizg.geofind.data.access.api.service;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.io.IOException;
import java.util.HashMap;

import retrofit2.Response;

public class PlayService {

	private static RestClient restClient;

	private static PlayService playService;

	private final String TAG = PlayService.class.getSimpleName();

	void instantiate(Application application) {
		if(restClient == null) {
			restClient = RetrofitInstance.getRestClient(application);
		}
		if(playService == null) {
			playService = new PlayService();
		}
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
			HashMap<String, String> params = new HashMap<>();
			params.put("user_id", String.valueOf(user_id));
			params.put("tour_id", String.valueOf(tour_id));
			response = restClient.getUserPlay(params).execute();
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
			HashMap<String, String> params = new HashMap<>();
			params.put("user_id", String.valueOf(user_id));
			params.put("tour_id", String.valueOf(tour_id));
			response = restClient.createUserPlay(params).execute();
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
			PlacePlay pp = new PlacePlay(place_id, play_id);
			response = restClient.createPlacePlay(play_id, pp).execute();
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
}
