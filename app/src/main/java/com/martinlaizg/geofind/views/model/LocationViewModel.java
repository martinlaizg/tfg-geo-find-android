package com.martinlaizg.geofind.views.model;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class LocationViewModel extends AndroidViewModel {

    private LocationRepository locRepo;
    private LiveData<List<Location>> allLocations;


    public LocationViewModel(@NonNull Application application) {
        super(application);
        locRepo = new LocationRepository(application);
        allLocations = locRepo.getAllLocations();
    }

    public LiveData<List<Location>> getAllLocations() {
        return allLocations;
    }
}
