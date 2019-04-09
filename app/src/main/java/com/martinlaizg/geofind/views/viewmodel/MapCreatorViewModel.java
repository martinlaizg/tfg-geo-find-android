package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.data.repository.LocationRepository;
import com.martinlaizg.geofind.data.repository.MapRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapCreatorViewModel
		extends AndroidViewModel {

	private final MapRepository mapRepo;
	private final LocationRepository locRepo;
	private Map createdMap;
	private List<Location> createdLocations;
	private boolean edit;
	private APIException error;


	public MapCreatorViewModel(@NonNull Application application) {
		super(application);
		mapRepo = new MapRepository(application);
		locRepo = new LocationRepository(application);
		edit = false;
	}


	public MutableLiveData<Map> createMap() {
		MutableLiveData<Map> m = new MutableLiveData<>();
		new Thread(() -> {
			// TODO manage errors
			// TODO create map and location on the same request
			if (createdMap.getId().isEmpty()) { // Create map
				try {
					createdMap = mapRepo.create(createdMap);
					if (!createdLocations.isEmpty()) {  // there are locations
						try {
							locRepo.createMapLocations(createdMap.getId(), createdLocations);
						} catch (APIException e) {  // fails create location
							if (!createdMap.getId().isEmpty()) { // is needed remove de created map
								mapRepo.remove(createdMap);
							}
							setError(e);
							m.postValue(null);
						}
					}
				} catch (APIException e) {  // fails create map
					setError(e);
					m.postValue(null);
					return;
				}

			} else { // Update map
				try {
					Map updatedMap = mapRepo.update(createdMap);
					if (!createdLocations.isEmpty()) {  // there are locations
						try {
							locRepo.updateMapLocations(createdMap.getId(), createdLocations);
							createdMap = updatedMap;
						} catch (APIException e) {  // fails create location
							if (!updatedMap.getId().isEmpty()) { // is needed revert the changes
								mapRepo.update(updatedMap);
							}
							setError(e);
							m.postValue(null);
						}
					} else {
						createdMap = updatedMap;
					}
				} catch (APIException e) {  // fails create map
					setError(e);
					m.postValue(null);
					return;
				}
			}
			m.postValue(createdMap);
		}).start();
		return m;
	}

	public List<Location> getCreatedLocations() {
		if (createdLocations == null) createdLocations = new ArrayList<>();
		return createdLocations;
	}

	public void setCreatedLocations(List<Location> createdLocations) {
		this.createdLocations = createdLocations;
	}

	public Map getCreatedMap() {
		if (createdMap == null) createdMap = new Map();
		return createdMap;
	}

	public void setCreatedMap(Map createdMap) {
		this.createdMap = createdMap;
	}

	public void addLocation(String name, String description, String lat, String lon, int position) {
		Location l = new Location();
		l.setName(name);
		l.setDescription(description);
		l.setLat(lat);
		l.setLon(lon);
		if (createdLocations == null) {
			createdLocations = new ArrayList<>();
		}
		l.setPosition(position);
		if (position >= createdLocations.size()) {
			createdLocations.add(l);
		} else {
			createdLocations.set(position, l);
		}
	}

	public void setCreatedMap(String name, String description, String creator_id, PlayLevel pl) {
		if (createdMap == null) {
			createdMap = new Map();
		}
		createdMap.setName(name);
		createdMap.setDescription(description);
		createdMap.setCreator_id(creator_id);
		createdMap.setMin_level(pl);
	}

	public MutableLiveData<Map> getMap(String map_id) {
		MutableLiveData<Map> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				m.postValue(mapRepo.getMap(map_id));
			} catch (APIException e) {
				setError(e);
				m.postValue(null);
			}
		}).start();
		return m;
	}

	public MutableLiveData<List<Location>> getLocations(String map_id) {
		MutableLiveData<List<Location>> locs = new MutableLiveData<>();
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
		createdMap = null;
		createdLocations = null;
	}

	public boolean isEdit() {
		return edit;
	}

	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
