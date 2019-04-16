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

public class MapService {

	private static MapService mapService;
	private static RestClient restClient;
	private final String TAG = MapService.class.getSimpleName();

	public static MapService getInstance() {
		if (restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if (mapService == null) {
			mapService = new MapService();
		}
		return mapService;
	}

	public List<Tour> getAllMaps() throws APIException {
		Response<List<Tour>> response;
		APIException apiException;
		try {
			response = restClient.getMaps(new HashMap<>()).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTours: ", e);
		}
		throw apiException;
	}

	public Tour create(Tour tour) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.createMap(tour).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "create: ", e);
		}
		throw apiException;
	}

	public Tour getMap(Integer id) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.getMap(id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getTour: ", e);
		}
		throw apiException;
	}

	public Tour update(Tour tourEntity) throws APIException {
		Response<Tour> response;
		APIException apiException;
		try {
			response = restClient.update(tourEntity.getId(), tourEntity).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "update: ", e);
		}
		throw apiException;
	}
}
