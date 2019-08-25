package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.repository.PlayRepository;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.ArrayList;
import java.util.List;

public class TourViewModel
		extends AndroidViewModel {

	private final TourRepository tourRepo;
	private final PlayRepository playRepo;

	private ErrorType error;
	private Tour tour;
	private Play play;

	public TourViewModel(@NonNull Application application) {
		super(application);
		tourRepo = TourRepository.getInstance(application);
		playRepo = PlayRepository.getInstance(application);
	}

	public MutableLiveData<Tour> getTour(int tour_id, int user_id) {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				tour = tourRepo.getTour(tour_id);
				play = playRepo.getPlay(user_id, tour_id);
			} catch(APIException e) {
				setError(e.getType());
				tour = null;
			}
			m.postValue(tour);
		}).start();
		return m;
	}

	public MutableLiveData<Place> getPlace(int place_id) {
		MutableLiveData<Place> p = new MutableLiveData<>();
		new Thread(() -> {
			for(Place place : tour.getPlaces()) {
				if(place.getId() == place_id) {
					p.postValue(place);
					return;
				}
			}
		}).start();
		return p;
	}

	public ErrorType getError() {
		return error;
	}

	private void setError(ErrorType error) {
		this.error = error;
	}

	public List<Place> getPlaces() {
		return tour.getPlaces();
	}

	@NonNull
	public List<Place> getPlayPlaces() {
		return play == null ?
				new ArrayList<>() :
				play.getPlaces();
	}
}
