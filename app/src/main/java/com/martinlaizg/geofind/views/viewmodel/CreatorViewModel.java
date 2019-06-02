package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
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
	private boolean load;

	public CreatorViewModel(@NonNull Application application) {
		super(application);
		tourRepo = RepositoryFactory.getTourRepository(application);
		load = true;
	}

	public MutableLiveData<Tour> createTour() {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			if(!tour.isValid()) {
				setError(new APIException(ErrorType.OTHER, "No data"));
				m.postValue(null);
				return;
			}
			load = true;
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

	public void setCreatedTour(String name, String description, Integer creator_id, PlayLevel pl) {
		if(tour == null) tour = new Tour();
		tour.setName(name);
		tour.setDescription(description);
		tour.setCreator_id(creator_id);
		tour.setMin_level(pl);
	}

	public MutableLiveData<Tour> loadTour(Integer tour_id) {
		MutableLiveData<Tour> t = new MutableLiveData<>();
		new Thread(() -> {
			if(load) {
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
			load = true;
			t.postValue(tour);
		}).start();
		return t;
	}

	public Tour getTour() {
		return tour;
	}

	// ============================================

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	public List<Place> getPlaces() {
		List<Place> places = tour.getPlaces();
		return places == null ?
				new ArrayList<>() :
				places;
	}

	public Place getPlace(int position) {
		if(position > tour.getPlaces().size()) return null;
		if(position < tour.getPlaces().size()) return tour.getPlaces().remove(position);
		return new Place();
	}

	public void setPlace(Place place) {
		if(place.getOrder() == null) {
			place.setOrder(tour.getPlaces().size());
			tour.getPlaces().add(place);
		} else {
			tour.getPlaces().add(place.getOrder(), place);
		}
	}
}
