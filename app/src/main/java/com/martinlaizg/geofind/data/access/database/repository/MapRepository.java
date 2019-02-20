package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;

import java.util.List;

import androidx.lifecycle.LiveData;

public class MapRepository {
    private MapDAO mapDAO;
    private LiveData<List<Map>> allMaps;

    public MapRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        mapDAO = database.mapDAO();
        allMaps = mapDAO.getAllMaps();
    }

    public void insert(User user) {

    }

    public void update(User user) {

    }

    public void delete(User user) {

    }

    public void deleteAllUsers() {

    }

}
