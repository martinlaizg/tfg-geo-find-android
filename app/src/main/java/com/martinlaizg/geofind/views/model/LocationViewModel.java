package com.martinlaizg.geofind.views.model;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository repository;
    private LiveData<List<Location>> allLocations;


    public LocationViewModel(@NonNull Application application) {
        super(application);
        repository = new LocationRepository(application);
        allLocations = repository.getAllLocations();
    }

    public void insert(Location location) {
        repository.insert(location);
    }

    public void update(Location location) {
        repository.update(location);
    }

    public void delete(Location location) {
        repository.delete(location);
    }

    public void deleteAllLocations() {
        repository.deleteAllLocations();
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }
}
