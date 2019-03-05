package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.retrofit.service.LocationService;

import java.util.List;

import androidx.lifecycle.LiveData;

public class LocationRepository {

    private LocationService locationService;

    private LocationDAO locDAO;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        locDAO = database.locationDAO();
        locationService = LocationService.getInstance();
    }

    public LiveData<List<Location>> getAllLocations() {
        LiveData<List<Location>> allLocations = locDAO.getAllLocations();
        if (allLocations != null && allLocations.getValue() != null && allLocations.getValue().isEmpty()) {
            allLocations.getValue().addAll(locationService.getAllLocations());
        }
        return allLocations;
    }

    public void create(List<Location> locations) {
        for (Location loc : locations) {
            locationService.create(loc);
        }
    }

    public LiveData<List<Location>> getLocationsByMap(String map_id) {
        LiveData<List<Location>> locations = locDAO.getLocationsByMap(map_id);
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (locations != null && locations.getValue() != null) {
                    for (Location loc : locationService.getAllLocations()) {
                        locDAO.insert(loc);
                    }
                }
            }
        }).start();
        return locations;
    }
}
