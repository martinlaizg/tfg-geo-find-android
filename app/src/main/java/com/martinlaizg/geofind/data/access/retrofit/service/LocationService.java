package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;
import com.martinlaizg.geofind.data.access.retrofit.error.APIError;
import com.martinlaizg.geofind.data.access.retrofit.error.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class LocationService {

	private static LocationService locationService;
	private static RestClient restClient;
	private final String TAG = LocationService.class.getSimpleName();

	public static LocationService getInstance() {
		if (restClient == null) {
			restClient = RetrofitService.getRestClient();
		}
		if (locationService == null) {
			locationService = new LocationService();
		}
		return locationService;
	}

	public List<Location> getAllLocations() {
		Response<List<Location>> response = null;
		try {
			response = restClient.getLocations(new HashMap<>()).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			APIError apiError = ErrorUtils.parseError(response);
			Location l = new Location();
			l.setError(apiError);
			return Collections.singletonList(l);
		} catch (IOException ex) {
			Log.e(TAG, "getAllLocations: ", ex);
		}
		return new ArrayList<>();
	}

	public void create(Location loc) {
		try {
			Response<Location> response = restClient.createLocation(loc).execute();
			if (!response.isSuccessful()) {
				APIError apiError = ErrorUtils.parseError(response);
			}
		} catch (IOException e) {
			Log.e(TAG, "createLocation: ", e);
		}
	}

	public List<Location> getLocationsByMap(String id) {
		Response<List<Location>> response = null;
		try {
			response = restClient.getLocationsByMap(id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			APIError apiError = ErrorUtils.parseError(response);
		} catch (IOException ex) {
			Log.e(TAG, "getLocationsByMap: ", ex);
		}
		return new ArrayList<>();
	}

	public Location getLocation(String loc_id) {
		Response<Location> response = null;
		try {
			response = restClient.getLocation(loc_id).execute();
			if (response.isSuccessful()) {
				return response.body();
			}
			APIError apiError = ErrorUtils.parseError(response);
		} catch (IOException ex) {
			Log.e(TAG, "getLocationsByMap: ", ex);
		}
		return new Location();
	}
}
