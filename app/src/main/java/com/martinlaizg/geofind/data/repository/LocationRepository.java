package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.LocationService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

	private final LocationService locationService;

	private final LocationDAO locDAO;
	private LiveData<List<PlaceEntity>> allLocations;

	public LocationRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		locDAO = database.locationDAO();
		locationService = LocationService.getInstance();
	}

	public LiveData<List<PlaceEntity>> getAllLocations() {
		return new MutableLiveData<>();
	}

	public List<PlaceEntity> createMapLocations(String id, List<PlaceEntity> locationEntities) throws APIException {
		return locationService.createMapLocations(id, locationEntities);
	}

	public List<PlaceEntity> updateMapLocations(String id, List<PlaceEntity> locationEntities) throws APIException {
		locationEntities = locationService.updateMapLocations(id, locationEntities);
		if (locationEntities != null) {
			locationEntities.forEach(locDAO::update);
		}
		return locationEntities;
	}

	public List<PlaceEntity> getLocationsByMap(String map_id) throws APIException {
		List<PlaceEntity> locs = locDAO.getPlacesByTour(map_id);
		if (locs == null || locs.isEmpty()) {
			locs = locationService.getLocationsByMap(map_id);
			if (locs != null && !locs.isEmpty()) {
				for (PlaceEntity l : locs) {
					locDAO.insert(l);
				}
				locs = locDAO.getPlacesByTour(map_id);
			}
		}
		return locs;
	}

	public PlaceEntity getLocation(String loc_id) throws APIException {
		PlaceEntity loc = locDAO.getLocation(loc_id);
		if (loc == null) {
			loc = locationService.getLocation(loc_id);
			if (loc != null) {
				locDAO.insert(loc);
				loc = locDAO.getLocation(loc_id);
			}
		}
		return loc;
	}
}
