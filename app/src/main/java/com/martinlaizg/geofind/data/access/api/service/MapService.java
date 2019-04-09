package com.martinlaizg.geofind.data.access.api.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entity.Map;

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

	public List<Map> getAllMaps() throws APIException {
		Response<List<Map>> response;
		APIException apiException;
		try {
			response = restClient.getMaps(new HashMap<>()).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);

		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getAllMaps: ", e);
		}
		throw apiException;
	}

	public Map create(Map map) throws APIException {
		Response<Map> response;
		APIException apiException;
		try {
			response = restClient.createMap(map).execute();
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

	public Map getMap(String id) throws APIException {
		Response<Map> response;
		APIException apiException;
		try {
			response = restClient.getMap(id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			apiException = ErrorUtils.parseError(response);
		} catch (IOException e) {
			apiException = new APIException(ErrorType.NETWORK, e.getMessage());
			Log.e(TAG, "getMap: ", e);
		}
		throw apiException;
	}

	public Map update(Map map) throws APIException {
		Response<Map> response;
		APIException apiException;
		try {
			response = restClient.update(map.getId(), map).execute();
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
