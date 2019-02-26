package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.retrofit.service.MapService;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MapRepository {


    private MapService mapService;
    private MapDAO mapDAO;

    private LocationDAO locDAO;


    private LiveData<List<Map>> allMaps;

    public MapRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        mapDAO = database.mapDAO();
        mapService = MapService.getInstance();
        locDAO = database.locationDAO();
    }


    public LiveData<List<Map>> getAllMaps() {
        return mapDAO.getAllMaps();
    }

    public LiveData<Map> getMap(String id) {
        return mapDAO.getMap(id);
    }

    public void refreshMaps() {
        List<Map> maps = mapService.getAllMaps();
        for (Map map : maps) {
            mapDAO.insert(map);
        }
    }

    public void insert(Map map) {
        mapDAO.insert(map);
    }


    public LiveData<List<Location>> getLocations(String map_id) {

        LiveData<List<Location>> locations = mapDAO.getLocations(map_id);

        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Location> serviceLocations = mapService.getLocations(map_id);
                for (Location loc : serviceLocations) {
                    locDAO.insert(loc);
                }
            }
        }).start();

        return locations;
    }
}
