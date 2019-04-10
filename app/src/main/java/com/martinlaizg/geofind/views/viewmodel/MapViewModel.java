package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
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

	public MutableLiveData<TourEntity> getMap(String map_id) {
		MutableLiveData<TourEntity> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				m.postValue(mapRepo.getMap(map_id));
			} catch (APIException e) {
				setError(e);
				m.postValue(null);
			}
		}).start();
		return m;
	}

	public MutableLiveData<List<PlaceEntity>> getLocations(String map_id) {
		MutableLiveData<List<PlaceEntity>> locs = new MutableLiveData<>();
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
}
