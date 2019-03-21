package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapViewModel extends AndroidViewModel {

    private MutableLiveData<Map> map;
    private MutableLiveData<List<Location>> locations;

    private MapRepository mapRepo;
    private LocationRepository locRepo;


    public MapViewModel(@NonNull Application application) {
        super(application);
        mapRepo = new MapRepository(application);
        locRepo = new LocationRepository(application);
        map = new MutableLiveData<>();
        locations = new MutableLiveData<>();
    }

    public MutableLiveData<Map> getMap(String map_id) {
        map = mapRepo.getMap(map_id);
        return map;
    }

    public Map getMap() {
        return map.getValue();
    }

    public MutableLiveData<List<Location>> getLocations(String map_id) {
        locations = locRepo.getLocationsByMap(map_id);
        return locations;
    }

    public List<Location> getLocations() {
        return locations.getValue();
    }
}
