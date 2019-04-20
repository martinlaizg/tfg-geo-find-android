package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.LocationService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

	private final LocationService locationService;

	private final PlaceDAO locDAO;
	private LiveData<List<Place>> allLocations;

	public LocationRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		locDAO = database.locationDAO();
		locationService = LocationService.getInstance();
	}

	public LiveData<List<Place>> getAllLocations() {
		return new MutableLiveData<>();
	}

	public List<Place> createMapLocations(Integer id, List<Place> locationEntities) throws APIException {
		return locationService.createMapLocations(id, locationEntities);
	}

	public List<Place> updateMapLocations(Integer id, List<Place> locationEntities) throws APIException {
		locationEntities = locationService.updateMapLocations(id, locationEntities);
		if (locationEntities != null) {
			locationEntities.forEach(locDAO::update);
		}
		return locationEntities;
	}

	public List<Place> getLocationsByMap(Integer map_id) throws APIException {
		List<Place> locs = locDAO.getPlacesByTour(map_id);
		if (locs == null || locs.isEmpty()) {
			locs = locationService.getLocationsByMap(map_id);
			if (locs != null && !locs.isEmpty()) {
				for (Place l : locs) {
					locDAO.insert(l);
				}
				locs = locDAO.getPlacesByTour(map_id);
			}
		}
		return locs;
	}

	public Place getLocation(Integer place_id) throws APIException {
		Place place = locDAO.getPlace(place_id);
		if (place == null) {
			place = locationService.getLocation(place_id);
			if (place != null) {
				locDAO.insert(place);
				place = locDAO.getPlace(place_id);
			}
		}
		return place;
	}
}
