package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

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

	private APIException error;
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
				setError(e);
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

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}

	public List<Place> getPlaces() {
		return tour.getPlaces();
	}

	public List<Place> getCompletedPlaces() {
		if(play != null) return play.getPlaces();
		return new ArrayList<>();
	}

	public List<Place> getNoCompletedPlaces() {
		List<Place> places = tour.getPlaces();
		if(play == null) {
			return places;
		}
		List<Place> noCompleted = new ArrayList<>();
		for(Place tp : places) {
			boolean completed = false;
			for(Place pp : play.getPlaces()) {
				if(tp.getId() == pp.getId()) completed = true;
			}
			if(!completed) noCompleted.add(tp);
		}
		return noCompleted;
	}

	public MutableLiveData<List<Place>> getPlaces(int tour_id, int user_id) {
		MutableLiveData<List<Place>> m = new MutableLiveData<>();
		new Thread(() -> m.postValue(new ArrayList<>())).start();
		return m;
	}
}
