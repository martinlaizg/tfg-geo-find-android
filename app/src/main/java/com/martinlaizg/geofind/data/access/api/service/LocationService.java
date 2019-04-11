package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class LocationService {

	private static LocationService locationService;
	private static RestClient restClient;
	private final String TAG = LocationService.class.getSimpleName();

	public static LocationService getInstance() {
		if (restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if (locationService == null) {
			locationService = new LocationService();
		}
		return locationService;
	}

	public List<Place> getAllLocations() throws APIException {
		Response<List<Place>> response;
		APIException apiException;
		try {
			response = restClient.getLocations(new HashMap<>()).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getAllLocations: ", e);
		}
		throw apiException;
	}

	public List<Place> createMapLocations(Integer id, List<Place> locationEntities) throws APIException {
		Response<List<Place>> response;
		APIException apiException;
		try {
			response = restClient.createMapLocations(id, locationEntities).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "createMapLocations: ", e);
		}
		throw apiException;
	}

	public List<Place> updateMapLocations(Integer id, List<Place> locationEntities) throws APIException {
		Response<List<Place>> response;
		APIException apiException;
		try {
			response = restClient.updateMapLocations(id, locationEntities).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "updateMapLocations: ", e);
		}
		throw apiException;
	}

	public List<Place> getLocationsByMap(Integer id) throws APIException {
		Response<List<Place>> response;
		APIException apiException;
		try {
			response = restClient.getMapLocations(id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getPlacesByTour: ", e);
		}
		throw apiException;
	}

	public Place getLocation(Integer loc_id) throws APIException {
		Response<Place> response;
		APIException apiException;
		try {
			response = restClient.getLocation(loc_id).execute();
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
