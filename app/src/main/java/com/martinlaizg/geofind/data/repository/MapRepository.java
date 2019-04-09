package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.MapService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.entity.Map;

import java.util.List;

public class MapRepository {

	private final MapService mapService;
	private final MapDAO mapDAO;

	public MapRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		mapDAO = database.mapDAO();
		mapService = MapService.getInstance();
	}

	public boolean remove(Map createdMap) {
		// TODO remove map
		return true;
	}

	public List<Map> getAllMaps() throws APIException {
		List<Map> ms = mapDAO.getAllMaps();
		if (ms == null || ms.isEmpty()) {
			ms = mapService.getAllMaps();
			if (ms != null) {
				for (Map m : ms) {
					mapDAO.insert(m);
				}
			}
		}
		return ms;
	}

	public Map getMap(String id) throws APIException {
		Map m = mapDAO.getMap(id);
		if (m == null) {
			m = mapService.getMap(id);
		}
		return m;
	}

	public Map create(Map map) throws APIException {
		map = mapService.create(map);
		if (map != null) mapDAO.insert(map);
		return map;
	}

	public Map update(Map map) throws APIException {
		map = mapService.update(map);
		if (map != null) mapDAO.update(map);
		return map;
	}
}
