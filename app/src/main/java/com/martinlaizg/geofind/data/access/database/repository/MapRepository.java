package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.retrofit.service.MapService;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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

    public MutableLiveData<Map> getMap(String id) {
        MutableLiveData<Map> map = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Map m = mapDAO.getMap(id);
                if (m == null) {
                    m = mapService.getMap(id);
                }
                map.postValue(m);
            }

        }).start();
        return map;
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

    public Map create(Map map) {
        Map map1 = mapService.create(map);
        refreshMaps();
        return map1;
    }
}
