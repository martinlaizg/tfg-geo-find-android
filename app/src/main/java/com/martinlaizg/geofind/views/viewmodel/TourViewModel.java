package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.List;

public class TourViewModel
		extends AndroidViewModel {

	private final TourRepository tourRepo;
	private APIException error;

	private Tour tour;

	public TourViewModel(@NonNull Application application) {
		super(application);
		tourRepo = new TourRepository(application);
	}

	public MutableLiveData<Tour> loadTour(Integer tour_id) {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		if(tour == null || !tour.getId().equals(tour_id)) {
			new Thread(() -> {
				try {
					tour = tourRepo.getTour(tour_id);
				} catch(APIException e) {
					setError(e);
					tour = null;
				}
				m.postValue(tour);
			}).start();
		} else {
			m.postValue(tour);
		}
		return m;
	}

	public MutableLiveData<Place> loadPlace(int place_id) {
		MutableLiveData<Place> p = new MutableLiveData<>();
		new Thread(() -> {
			for(Place place : tour.getPlaces()) {
				if(place.getId().equals(place_id)) {
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

	public List<Place> getPlaces() {
		return tour.getPlaces();
	}
}
