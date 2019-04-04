package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.retrofit.service.MapService;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public class MapRepository {

	private final MapService mapService;
	private final MapDAO mapDAO;

	public MapRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		mapDAO = database.mapDAO();
		mapService = MapService.getInstance();
	}

	public MutableLiveData<List<Map>> getAllMaps() {
		MutableLiveData<List<Map>> maps = new MutableLiveData<>();
		new Thread(() -> {
			List<Map> ms = mapDAO.getAllMaps();
			if (ms == null || ms.isEmpty()) {
				ms = mapService.getAllMaps();
				if (ms != null) {
					for (Map m : ms) {
						mapDAO.insert(m);
					}
				}
			}
			maps.postValue(ms);
		}).start();
		return maps;
	}

	public MutableLiveData<Map> getMap(String id) {
		MutableLiveData<Map> map = new MutableLiveData<>();
		new Thread(() -> {
			Map m = mapDAO.getMap(id);
			if (m == null) {
				m = mapService.getMap(id);
			}
			map.postValue(m);
		}).start();
		return map;
	}

	public void refreshMaps() {
		List<Map> maps = mapService.getAllMaps();
		for (Map map : maps) {
			mapDAO.insert(map);
		}
	}

	public Map create(Map map) {
		map = mapService.create(map);
		if (map != null) mapDAO.insert(map);
		return map;
	}

	public Map update(Map map) {
		map = mapService.update(map);
		if (map != null) mapDAO.update(map);
		return map;
	}
}
