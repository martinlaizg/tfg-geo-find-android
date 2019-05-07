package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.List;

public class TourListViewModel
		extends AndroidViewModel {

	private final TourRepository repository;
	private APIException error;

	public TourListViewModel(@NonNull Application application) {
		super(application);
		repository = new TourRepository(application);
	}

	public MutableLiveData<List<TourCreatorPlaces>> getTours() {
		MutableLiveData<List<TourCreatorPlaces>> tours = new MutableLiveData<>();
		new Thread(() -> {
			try {
				tours.postValue(repository.getAllTours());
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
