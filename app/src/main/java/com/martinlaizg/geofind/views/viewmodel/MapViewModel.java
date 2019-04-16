package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.LocationRepository;
import com.martinlaizg.geofind.data.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapViewModel
		extends AndroidViewModel {

	private final MapRepository mapRepo;
	private final LocationRepository locRepo;
	private APIException error;


	public MapViewModel(@NonNull Application application) {
		super(application);
		mapRepo = new MapRepository(application);
		locRepo = new LocationRepository(application);
	}

	public MutableLiveData<Tour> getMap(Integer map_id) {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				m.postValue(mapRepo.getTour(map_id));
			} catch (APIException e) {
				setError(e);
				m.postValue(null);
			}
		}).start();
		return m;
	}

	public MutableLiveData<List<Place>> getLocations(Integer map_id) {
		MutableLiveData<List<Place>> locs = new MutableLiveData<>();
		new Thread(() -> {
			try {
				locs.postValue(locRepo.getLocationsByMap(map_id));
			} catch (APIException e) {
				setError(e);
				locs.postValue(null);
			}

		}).start();
		return locs;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

	public MutableLiveData<Place> getPlace(int place_id) {
		MutableLiveData<Place> place = new MutableLiveData<>();
		new Thread(() -> {
			try {
				place.postValue(locRepo.getLocation(place_id));
			} catch (APIException e) {
				setError(e);
				place.postValue(null);
			}

		}).start();
		return place;
	}
}
