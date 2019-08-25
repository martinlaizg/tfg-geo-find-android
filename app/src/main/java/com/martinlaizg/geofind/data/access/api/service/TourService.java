package com.martinlaizg.geofind.data.access.api.service;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class TourService {

	private static RestClient restClient;

	private static TourService instance;

	private final String TAG = TourService.class.getSimpleName();

	private TourService(Application application) {
		if(restClient == null) {
			restClient = RetrofitInstance.getRestClient(application);
		}
	}

	public static TourService getInstance(Application application) {
		if(instance == null) instance = new TourService(application);
		return instance;
	}

	/**
	 * Retrieve all tours form de server
	 *
	 * @return the list of tours
	 * @throws APIException
	 * 		server errors
	 */
	public List<Tour> getAllTours() throws APIException {
		Response<List<Tour>> response;
		APIException apiException;
		try {
			response = restClient.getTours(new HashMap<>()).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTours: ", e);
		}
		throw apiException;
	}

	/**
	 * Create de tour in the server
	 *
	 * @param tour
	 * 		the tour to create
	 * @return the created tour
	 * @throws APIException
	 * 		server errors
	 */
	public Tour create(Tour tour) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.createTour(tour).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "create: ", e);
		}
		throw apiException;
	}

	/**
	 * Get a single tour by id
	 *
	 * @param id
	 * 		the id of the Tour to get
	 * @return the tour
	 * @throws APIException
	 * 		server errors
	 */
	public Tour getTour(Integer id) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.getTour(id).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTour: ", e);
		}
		throw apiException;
	}

	/**
	 * Update the tour with id=tour.getId() with the data in tour
	 *
	 * @param tour
	 * 		the new data
	 * @return the new tour
	 * @throws APIException
	 * 		server errors
	 */
	public Tour update(Tour tour) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.update(tour.getId(), tour).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "update: ", e);
		}
		throw apiException;
	}

	public List<Tour> getTours(String stringQuery) throws APIException {
		Response<List<Tour>> response;
		APIException apiException;
		HashMap<String, String> params = new HashMap<>();
		params.put("q", stringQuery);
		try {
			response = restClient.getTours(params).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(Exception e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "update: ", e);
		}
		throw apiException;
	}
}
