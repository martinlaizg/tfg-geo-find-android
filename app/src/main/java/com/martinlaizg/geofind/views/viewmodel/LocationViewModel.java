package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.repository.LocationRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class LocationViewModel
		extends AndroidViewModel {

	private final LocationRepository locRepo;
	private final LiveData<List<Location>> allLocations;
	private APIException error;


	public LocationViewModel(@NonNull Application application) {
		super(application);
		locRepo = new LocationRepository(application);
		allLocations = locRepo.getAllLocations();
	}

	public LiveData<List<Location>> getAllLocations() {
		return allLocations;
	}

	public MutableLiveData<Location> getLocation(String loc_id) {
		MutableLiveData<Location> l = new MutableLiveData<>();

		new Thread(() -> {
			try {
				l.postValue(locRepo.getLocation(loc_id));
			} catch (APIException e) {
				setError(e);
				l.postValue(null);
			}

		}).start();

		return l;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
