package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.List;

public class MapListViewModel
		extends AndroidViewModel {

	private final TourRepository repository;
	private APIException error;

	public MapListViewModel(@NonNull Application application) {
		super(application);
		repository = new TourRepository(application);
	}

	public MutableLiveData<List<Tour>> getTours() {
		MutableLiveData<List<Tour>> tours = new MutableLiveData<>();
		new Thread(() -> {
			try {
				tours.postValue(repository.getAllMaps());
			} catch(APIException e) {
				setError(e);
				tours.postValue(null);
			}
		}).start();
		return tours;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
