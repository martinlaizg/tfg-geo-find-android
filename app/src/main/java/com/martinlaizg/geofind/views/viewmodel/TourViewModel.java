package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.TourRepository;

public class TourViewModel
		extends AndroidViewModel {

	private final TourRepository tourRepo;
	private APIException error;

	private Tour tour;

	public TourViewModel(@NonNull Application application) {
		super(application);
		tourRepo = new TourRepository(application);
	}

	public MutableLiveData<Tour> loadTour(Integer map_id) {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				tour = tourRepo.getTour(map_id);
				m.postValue(tour);
			} catch (APIException e) {
				setError(e);
				m.postValue(null);
			}
		}).start();
		return m;
	}

	public MutableLiveData<Place> loadPlace(int place_id) {
		MutableLiveData<Place> p = new MutableLiveData<>();
		new Thread(() -> {
			for (Place place : tour.getPlaces()) {
				if (place.getId().equals(place_id)) {
					p.postValue(place);
					return;
				}
			}
		}).start();
		return p;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

}
