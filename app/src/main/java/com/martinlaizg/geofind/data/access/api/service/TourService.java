package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class TourService {

	private static TourService tourService;
	private static RestClient restClient;
	private final String TAG = TourService.class.getSimpleName();

	public static TourService getInstance() {
		if(restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if(tourService == null) {
			tourService = new TourService();
		}
		return tourService;
	}

	public List<Tour> getAllTours() throws APIException {
		Response<List<Tour>> response;
		APIException apiException;
		try {
			response = restClient.getTours(new HashMap<>()).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTours: ", e);
		}
		throw apiException;
	}

	public Tour create(Tour tour) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.createTour(tour).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "create: ", e);
		}
		throw apiException;
	}

	public Tour getTour(Integer id) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.getTour(id).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTour: ", e);
		}
		throw apiException;
	}

	public Tour update(Tour tour) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.update(tour.getId(), tour).execute();
			if(response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch(IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "update: ", e);
		}
		throw apiException;
	}
}
