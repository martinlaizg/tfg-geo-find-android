package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.retrofit.service.LocationService;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationRepository {

	private LocationService locationService;

	private LocationDAO locDAO;
	private LiveData<List<Location>> allLocations;

	public LocationRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		locDAO = database.locationDAO();
		locationService = LocationService.getInstance();
	}

	public LiveData<List<Location>> getAllLocations() {
		return new MutableLiveData<>();
	}

	public void create(List<Location> locations) {
		for (Location loc : locations) {
			locationService.create(loc);
		}
	}

	public MutableLiveData<List<Location>> getLocationsByMap(String map_id) {
		MutableLiveData<List<Location>> locations = new MutableLiveData<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
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
				locations.postValue(locs);
			}
		}).start();

		return locations;
	}

	public MutableLiveData<Location> getLocation(String loc_id) {
		MutableLiveData<Location> location = new MutableLiveData<>();
		new Thread(new Runnable() {
			@Override
			public void run() {
				Location loc = locDAO.getLocation(loc_id);
				if (loc == null) {
					loc = locationService.getLocation(loc_id);
					if (loc != null) {
						locDAO.insert(loc);
						loc = locDAO.getLocation(loc_id);
					}
				}
				location.postValue(loc);
			}
		}).start();
		return location;
	}
}
