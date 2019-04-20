package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.google.android.gms.maps.model.LatLng;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.data.repository.LocationRepository;
import com.martinlaizg.geofind.data.repository.MapRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CreatorViewModel
		extends AndroidViewModel {

	private final MapRepository mapRepo;
	private final LocationRepository locRepo;
	private Tour tour;
	private APIException error;
	private boolean load;


	public CreatorViewModel(@NonNull Application application) {
		super(application);
		mapRepo = new MapRepository(application);
		locRepo = new LocationRepository(application);
		load = true;
	}

	// ============================================

	public MutableLiveData<Tour> createTour() {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			if (!tour.isValid()) {
				setError(new APIException(ErrorType.OTHER, "Faltan datos"));
				m.postValue(null);
				return;
			}
			load = true;
			if (tour.getId() == 0) { // Create tour
				try {
					tour = mapRepo.create(tour);
				} catch (APIException e) {
					setError(e);
					m.postValue(null);
					return;
				}
			} else { // Update tour
				try {
					tour = mapRepo.update(tour);
				} catch (APIException e) {
					setError(e);
					m.postValue(null);
					return;
				}
			}
			m.postValue(tour);
		}).start();
		return m;
	}

	public List<Place> getPlaces() {
		return tour.getPlaces();
	}

	public void setLocation(String name, String description, LatLng position, int order) {
		Place p;
		if (order < tour.getPlaces().size()) { // Existing place
			p = tour.getPlaces().get(order);
		} else {
			p = new Place();
			tour.getPlaces().add(p);
		}
		p.setName(name);
		p.setDescription(description);
		p.setPosition(position);
		p.setOrder(order);
	}

	public void setCreatedMap(String name, String description, Integer creator_id, PlayLevel pl) {
		if(tour == null) tour = new Tour();
		tour.setName(name);
		tour.setDescription(description);
		tour.setCreator_id(creator_id);
		tour.setMin_level(pl);
	}

	public MutableLiveData<Tour> loadTour(Integer tour_id) {
		MutableLiveData<Tour> t = new MutableLiveData<>();
		new Thread(() -> {
			if (load) {
				tour = new Tour();
				if(tour_id>0) {
					try {
						tour = mapRepo.getTour(tour_id);
					} catch (APIException e) {
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

	// ============================================

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

	public void setLoad(boolean load) {
		this.load = load;
	}

	public boolean checkPlaceName(String newName) {
		for(Place p : getPlaces()){
			if(p.getName().equals(newName)) return false;
		}
		return true;
	}
}
