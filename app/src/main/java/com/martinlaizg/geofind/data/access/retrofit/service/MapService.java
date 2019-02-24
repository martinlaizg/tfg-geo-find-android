package com.martinlaizg.geofind.data.access.retrofit.service;

import android.util.Log;

import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.retrofit.RestClient;
import com.martinlaizg.geofind.data.access.retrofit.RetrofitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Response;

public class MapService {

    private static MapService mapService;
    private static RestClient restClient;
    private String TAG = MapService.class.getSimpleName();

    public static MapService getInstance() {
        if (restClient == null) {
            restClient = RetrofitService.getRestClient();
        }
        if (mapService == null) {
            mapService = new MapService();
        }
        return mapService;
    }

    public List<Map> getAllMaps() {
        try {
            Response<List<Map>> response = restClient.getMaps(new HashMap<>()).execute();
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, "getAllMaps: ", e);
        }
        return new ArrayList<>();
    }


    public Map getSingle(String id) {
        // TODO: unimplemented method
        return null;
    }

    public void insert(Map map) {
        // TODO: unimplemented method
    }

    public void update(Map map) {
        // TODO: unimplemented method
    }

    public void deleteAllMaps() {
        // TODO: unimplemented method
    }

    public void delete(Map map) {
        // TODO: unimplemented method
    }


}
