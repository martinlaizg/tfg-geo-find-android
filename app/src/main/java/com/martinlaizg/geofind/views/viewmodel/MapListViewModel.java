package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
import com.martinlaizg.geofind.data.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapListViewModel
		extends AndroidViewModel {

	private final MapRepository repository;
	private APIException error;


	public MapListViewModel(@NonNull Application application) {
		super(application);
		repository = new MapRepository(application);
	}


	public MutableLiveData<List<TourEntity>> getAllMaps() {
		MutableLiveData<List<TourEntity>> maps = new MutableLiveData<>();
		new Thread(() -> {
			try {
				maps.postValue(repository.getAllMaps());
			} catch (APIException e) {
				maps.postValue(null);
				setError(e);
			}
		}).start();
		return maps;
	}


	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
