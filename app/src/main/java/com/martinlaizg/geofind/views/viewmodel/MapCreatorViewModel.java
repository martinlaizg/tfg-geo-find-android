package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.data.access.database.repository.LocationRepository;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapCreatorViewModel extends AndroidViewModel {

    private Map createdMap;
    private List<Location> createdLocations;

    private MapRepository mapRepo;
    private LocationRepository locRepo;


    public MapCreatorViewModel(@NonNull Application application) {
        super(application);
        mapRepo = new MapRepository(application);
        locRepo = new LocationRepository(application);
    }


    public MutableLiveData<Map> createMap() {
        MutableLiveData<Map> m = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                createdMap = mapRepo.create(createdMap);
                for (int i = 0; i < createdLocations.size(); i++) {
                    createdLocations.get(i).setMap_id(createdMap.getId());
                }
                locRepo.create(createdLocations);
                m.postValue(createdMap);
            }
        }).start();
        return m;
    }

    public List<Location> getCreatedLocations() {
        return createdLocations;
    }

    public Map getCreatedMap() {
        if (createdMap == null) {
            createdMap = new Map();
        }
        return createdMap;
    }

    public void addLocation(String name, String lat, String lon) {
        Location l = new Location();
        l.setName(name);
        l.setLat(lat);
        l.setLon(lon);
        if (createdLocations == null) {
            createdLocations = new ArrayList<>();
        }
        createdLocations.add(l);
    }


    public boolean isMapCreated() {
        Map m = createdMap;
        return m != null && !m.getName().isEmpty() && !m.getDescription().isEmpty();
    }

    public void setMap(String name, String description, String creator_id, PlayLevel pl) {
        if (createdMap == null) {
            createdMap = new Map();
        }
        createdMap.setName(name);
        createdMap.setDescription(description);
        createdMap.setCreator_id(creator_id);
        createdMap.setMin_level(pl);
    }
}
