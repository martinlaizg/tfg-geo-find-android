package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;
import android.util.Log;

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
		new Thread(() -> tours.postValue(tourRepo.getAllTours())).start();
		return tours;
	}

	public void refresh() {
		new Thread(() -> {
			try {
				tourRepo.refreshTours();
			} catch(APIException e) {
				Log.e(TAG, "getTours: ", e);
				setError(e);
			}
		}).start();
	}

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}
}
