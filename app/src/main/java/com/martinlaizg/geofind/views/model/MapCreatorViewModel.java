package com.martinlaizg.geofind.views.model;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MapCreatorViewModel extends AndroidViewModel {

    private MapRepository repository;
    private LiveData<List<Location>> allLocations;


    public MapCreatorViewModel(@NonNull Application application) {
        super(application);
        repository = new MapRepository(application);
    }

    public void insert(Map map) {
        repository.insert(map);
    }
}
