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

    private LocationDAO locationDAO;
    private LiveData<List<Location>> allLocations;

    public LocationRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        locationDAO = database.locationDAO();
        locationService = LocationService.getInstance();
    }

    public void insert(Location location) {
        locationDAO.insert(location);
        locationService.insert(location);
    }

    public void update(Location location) {
        locationDAO.update(location);
        locationService.update(location);
    }

    public void delete(Location location) {
        locationDAO.update(location);
        locationService.update(location);

    }

    public void deleteAllLocations() {
        locationDAO.deleteAllLocations();
        locationService.deleteAllLocations();

    }

    public LiveData<List<Location>> getAllLocations() {
        LiveData<List<Location>> allLocations = locationDAO.getAllLocations();
        if (allLocations != null && allLocations.getValue() != null && allLocations.getValue().isEmpty()) {
            allLocations.getValue().addAll(locationService.getAllLocations());
        }
        return allLocations;
    }

}
