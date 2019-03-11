package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapCreatorViewModel extends AndroidViewModel {

    private MutableLiveData<Map> createdMap;
    private MutableLiveData<List<Location>> createdLocations;

    private MapRepository mapRepo;
    private LocationRepository locRepo;


    public MapCreatorViewModel(@NonNull Application application) {
        super(application);
        mapRepo = new MapRepository(application);
        locRepo = new LocationRepository(application);

        createdMap = new MutableLiveData<>();
        createdLocations = new MutableLiveData<>();
    }


    public void createMap() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                mapRepo.create(createdMap.getValue());
                locRepo.create(createdLocations.getValue());
            }
        }).start();
    }

    public MutableLiveData<List<Location>> getCreatedLocations() {
        return createdLocations;
    }

    public MutableLiveData<Map> getCreatedMap() {
        return createdMap;
    }

    public void addLocation(String name, String lat, String lon) {
        Location l = new Location();
        l.setName(name);
        l.setLat(lat);
        l.setLon(lon);

        List<Location> locs = createdLocations.getValue();
        if (locs == null) {
            locs = new ArrayList<>();
        }
        locs.add(l);
        createdLocations.setValue(locs);
    }


    public boolean isMapCreated() {
        Map m = createdMap.getValue();
        return m != null && !m.getName().isEmpty() && !m.getDescription().isEmpty();
    }

    public void setMap(Map map) {
        createdMap.setValue(map);
    }
}
