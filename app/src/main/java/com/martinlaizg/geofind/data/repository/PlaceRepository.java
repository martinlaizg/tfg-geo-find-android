package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.martinlaizg.geofind.data.access.api.service.LocationService;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

public class PlaceRepository {

	private final LocationService locationService;

	private final PlaceDAO locDAO;
	private LiveData<List<Place>> allLocations;

	public PlaceRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		locDAO = database.locationDAO();
		locationService = LocationService.getInstance();
	}

}
