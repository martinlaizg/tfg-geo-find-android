package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.List;

public class TourListViewModel
		extends AndroidViewModel {

	private static final String TAG = TourListViewModel.class.getSimpleName();

	private final TourRepository tourRepo;
	private APIException error;

	public TourListViewModel(@NonNull Application application) {
		super(application);
		tourRepo = RepositoryFactory.getTourRepository(application);
	}

	public MutableLiveData<List<Tour>> getTours() {
		MutableLiveData<List<Tour>> tours = new MutableLiveData<>();
		new Thread(() -> {
			try {
				tours.postValue(tourRepo.getAllTours());
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

	private void setError(APIException error) {
		this.error = error;
	}
}
