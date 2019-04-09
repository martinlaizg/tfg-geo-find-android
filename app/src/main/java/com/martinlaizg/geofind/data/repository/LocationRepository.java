package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.LocationService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

	private final LocationService locationService;

	private final LocationDAO locDAO;
	private LiveData<List<Location>> allLocations;

	public LocationRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		locDAO = database.locationDAO();
		locationService = LocationService.getInstance();
	}

	public LiveData<List<Location>> getAllLocations() {
		return new MutableLiveData<>();
	}

	public List<Location> createMapLocations(String id, List<Location> locations) throws APIException {
		return locationService.createMapLocations(id, locations);
	}

	public List<Location> updateMapLocations(String id, List<Location> locations) throws APIException {
		return locationService.updateMapLocations(id, locations);
	}

	public List<Location> getLocationsByMap(String map_id) throws APIException {
		List<Location> locs = locDAO.getLocationsByMap(map_id);
		if (locs == null || locs.isEmpty()) {
			locs = locationService.getLocationsByMap(map_id);
			if (locs != null && !locs.isEmpty()) {
				for (Location l : locs) {
					locDAO.insert(l);
				}
				locs = locDAO.getLocationsByMap(map_id);
			}
		}
		return locs;
	}

	public Location getLocation(String loc_id) throws APIException {
		Location loc = locDAO.getLocation(loc_id);
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
