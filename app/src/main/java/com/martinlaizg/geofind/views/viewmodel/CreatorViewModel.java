package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.data.repository.LocationRepository;
import com.martinlaizg.geofind.data.repository.MapRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class CreatorViewModel
		extends AndroidViewModel {

	private final MapRepository mapRepo;
	private final LocationRepository locRepo;
	private Tour tour;
	private List<Place> createdLocationEntities;
	private boolean loaded;
	private APIException error;


	public CreatorViewModel(@NonNull Application application) {
		super(application);
		mapRepo = new MapRepository(application);
		locRepo = new LocationRepository(application);
		loaded = false;
	}


	public MutableLiveData<Tour> createTour() {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			// TODO manage errors
			// TODO create tourEntity and location on the same request
			if (tour.getId() == 0) { // Create tourEntity
				try {
					tour = mapRepo.create((Tour) tour);
					if (!createdLocationEntities.isEmpty()) {  // there are locationEntities
						try {
							locRepo.createMapLocations(tour.getId(), createdLocationEntities);
						} catch (APIException e) {  // fails create location
							if (tour.getId() != 0) { // is needed remove de created tourEntity
								//								mapRepo.remove(tour);
							}
							setError(e);
							m.postValue(null);
						}
					}
				} catch (APIException e) {  // fails create tourEntity
					setError(e);
					m.postValue(null);
					return;
				}

			} else { // Update tourEntity
				try {
					Tour updatedTour = mapRepo.update((Tour) tour);
					if (!createdLocationEntities.isEmpty()) {  // there are locationEntities
						try {
							locRepo.updateMapLocations(tour.getId(), createdLocationEntities);
							tour = updatedTour;
						} catch (APIException e) {  // fails create location
							if (updatedTour.getId() != 0) { // is needed revert the changes
								mapRepo.update((Tour) updatedTour);
							}
							setError(e);
							m.postValue(null);
						}
					} else {
						tour = updatedTour;
					}
				} catch (APIException e) {  // fails create tourEntity
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
		if (createdLocationEntities == null) createdLocationEntities = new ArrayList<>();
		return createdLocationEntities;
	}

	public void setCreatedLocationEntities(List<Place> createdLocationEntities) {
		this.createdLocationEntities = createdLocationEntities;
	}

	public Tour getTour() {
		if (tour == null) tour = new Tour();
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public void setLocation(String name, String description, LatLng position, int order) {
		Place l = new Place();
		l.setName(name);
		l.setDescription(description);
		l.setPosition(position);
		l.setOrder(order);
		if (createdLocationEntities == null) {
			createdLocationEntities = new ArrayList<>();
		}

		if (order < createdLocationEntities.size()) {
			createdLocationEntities.set(order, l);
		} else {
			createdLocationEntities.add(l);
		}
	}

	public void setCreatedMap(String name, String description, Integer creator_id, PlayLevel pl) {
		if (tour == null) {
			tour = new Tour();
		}
		tour.setName(name);
		tour.setDescription(description);
		tour.setCreator_id(creator_id);
		tour.setMin_level(pl);
	}

	public MutableLiveData<Tour> getTour(Integer map_id) {
		MutableLiveData<Tour> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				m.postValue(mapRepo.getTour(map_id));
			} catch (APIException e) {
				setError(e);
				m.postValue(null);
			}
		}).start();
		return m;
	}

	public MutableLiveData<List<Place>> getLocations(Integer map_id) {
		MutableLiveData<List<Place>> locs = new MutableLiveData<>();
		new Thread(() -> {
			try {
				locs.postValue(locRepo.getLocationsByMap(map_id));
			} catch (APIException e) {
				setError(e);
				locs.postValue(null);
			}
		}).start();
		return locs;
	}

	public void clear() {
		tour = null;
		createdLocationEntities = null;
		loaded = false;
	}

	public boolean isLoaded() {
		return loaded;
	}

	public void setLoaded(boolean loaded) {
		this.loaded = loaded;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

	public MutableLiveData<Tour> loadTour(Bundle arguments) throws APIException {
		// TODO
		if (arguments == null) throw new APIException(ErrorType.OTHER, "No arguments");
		MutableLiveData<Tour> m = new MutableLiveData<>();
		return m;
	}
}
