package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.ArrayList;
import java.util.List;

public class CreatorViewModel
		extends AndroidViewModel {

	private final TourRepository tourRepo;
	private Tour tour;
	private APIException error;

	public CreatorViewModel(@NonNull Application application) {
		super(application);
		tourRepo = RepositoryFactory.getTourRepository(application);
	}

	public MutableLiveData<Tour> createTour() {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			if(tour.getId() == 0) { // Create tour
				try {
					tour = tourRepo.create(tour);
				} catch(APIException e) {
					setError(e);
					m.postValue(null);
					return;
				}
			} else { // Update tour
				try {
					tour = tourRepo.update(tour);
				} catch(APIException e) {
					setError(e);
					m.postValue(null);
					return;
				}
			}
			m.postValue(tour);
		}).start();
		return m;
	}

	public void updateTour(String name, String description, Integer creator_id, PlayLevel pl,
			String image_url) {
		if(tour == null) tour = new Tour();
		tour.setName(name);
		tour.setDescription(description);
		tour.setCreator_id(creator_id);
		tour.setMin_level(pl);
		tour.setImage(image_url);
	}

	public MutableLiveData<Tour> loadTour(Integer tour_id) {
		MutableLiveData<Tour> t = new MutableLiveData<>();
		new Thread(() -> {
			if(tour == null) {
				tour = new Tour();
				if(tour_id > 0) {
					try {
						tour = tourRepo.getTour(tour_id);
					} catch(APIException e) {
						setError(e);
						tour = null;
					}
				}
			}
			t.postValue(tour);
		}).start();
		return t;
	}

	public Tour getTour() {
		return tour;
	}

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}

	public List<Place> getPlaces() {
		List<Place> places = tour.getPlaces();
		return places == null ?
				new ArrayList<>() :
				places;
	}

	public Place getPlace(int position) {
		if(position > tour.getPlaces().size()) return null;
		if(position < tour.getPlaces().size()) return tour.getPlaces().get(position);
		return new Place();
	}

	public void setPlace(Place place) {
		if(place.getOrder() == null) {
			place.setOrder(tour.getPlaces().size());
			tour.getPlaces().add(place);
		} else {
			tour.getPlaces().set(place.getOrder(), place);
		}
	}

	public void reset() {
		tour = null;
	}

}
