package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;
import com.martinlaizg.geofind.data.access.retrofit.error.APIError;
import com.martinlaizg.geofind.data.access.retrofit.error.ErrorUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class LocationService {

    private static LocationService locationService;
    private static RestClient restClient;
    private String TAG = LocationService.class.getSimpleName();

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
        } catch (IOException ex) {
            Log.e(TAG, "getAllLocations: ", ex);
        }
        return new ArrayList<>();
    }

    public Location getSingle(int id) {
        // TODO: unimplemented method
        return null;
    }

    public void insert(Location location) {
        // TODO: unimplemented method
    }

    public void update(Location location) {
        // TODO: unimplemented method
    }

    public void deleteAllLocations() {
        // TODO: unimplemented method

    }

}
