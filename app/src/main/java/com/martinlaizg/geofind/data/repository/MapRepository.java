package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.MapService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.MapLocationsDAO;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
import com.martinlaizg.geofind.data.access.database.entities.relations.MapUsernameLocations;

import java.util.ArrayList;
import java.util.List;

public class MapRepository {

	private final MapService mapService;
	private final MapDAO mapDAO;
	private final MapLocationsDAO mapLocsDAO;
	private final LocationDAO locDAO;

	public MapRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		mapDAO = database.mapDAO();
		locDAO = database.locationDAO();
		mapLocsDAO = database.mapLocsDAO();
		mapService = MapService.getInstance();
	}

	public List<TourEntity> getAllMaps() throws APIException {
		List<MapUsernameLocations> mls = mapLocsDAO.getMapWithLocations();
		List<TourEntity> ms = new ArrayList<>();
		for (MapUsernameLocations ml : mls) {
			TourEntity m = ml.getTour();
			m.setPlaceEntities(ml.getLocationEntities());

		}

		if (ms == null || ms.isEmpty()) {
			ms = mapService.getAllMaps();
			if (ms != null) {
				for (TourEntity m : ms) {
					mapDAO.insert(m);
					for (PlaceEntity l : m.getPlaceEntities()) {
						locDAO.insert(l);
					}
				}
			}
		}
		return ms;
	}

	public TourEntity getMap(String id) throws APIException {
		TourEntity m = mapDAO.getMap(id);
		if (m == null) {
			m = mapService.getMap(id);
		}
		return m;
	}

	public TourEntity create(TourEntity tourEntity) throws APIException {
		tourEntity = mapService.create(tourEntity);
		if (tourEntity != null) mapDAO.insert(tourEntity);
		return tourEntity;
	}

	public TourEntity update(TourEntity tourEntity) throws APIException {
		tourEntity = mapService.update(tourEntity);
		if (tourEntity != null) mapDAO.update(tourEntity);
		return tourEntity;
	}
}
