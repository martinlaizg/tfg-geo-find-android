package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.retrofit.service.MapService;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MapRepository {


    private MapService mapService;
    private MapDAO mapDAO;


    private LiveData<List<Map>> allMaps;

    public MapRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        mapDAO = database.mapDAO();
        mapService = MapService.getInstance();
    }


    public LiveData<List<Map>> getAllMaps() {
        return mapDAO.getAllMaps();
    }

    public Map getMap(String id) {
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

}
