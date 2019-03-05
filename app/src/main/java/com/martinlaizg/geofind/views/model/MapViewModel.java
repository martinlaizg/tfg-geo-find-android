package com.martinlaizg.geofind.views.model;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class MapViewModel extends AndroidViewModel {

    private MapRepository mapRepo;
    private LocationRepository locRepo;
    private LiveData<Map> map;


    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRepo = new MapRepository(application);
        locRepo = new LocationRepository(application);
    }

    public LiveData<Map> getMap(String map_id) {
        return mapRepo.getMap(map_id);
    }

    public LiveData<List<Location>> getLocationsByMap(String map_id) {
        return locRepo.getLocationsByMap(map_id);
    }
}
