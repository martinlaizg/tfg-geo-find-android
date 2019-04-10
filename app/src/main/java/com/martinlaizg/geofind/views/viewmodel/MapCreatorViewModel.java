package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
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
	private TourEntity createdTourEntity;
	private List<PlaceEntity> createdLocationEntities;
	private boolean edit;
	private APIException error;


	public MapCreatorViewModel(@NonNull Application application) {
		super(application);
		mapRepo = new MapRepository(application);
		locRepo = new LocationRepository(application);
		edit = false;
	}


	public MutableLiveData<TourEntity> createMap() {
		MutableLiveData<TourEntity> m = new MutableLiveData<>();
		new Thread(() -> {
			// TODO manage errors
			// TODO create tourEntity and location on the same request
			if (createdTourEntity.getId().isEmpty()) { // Create tourEntity
				try {
					createdTourEntity = mapRepo.create(createdTourEntity);
					if (!createdLocationEntities.isEmpty()) {  // there are locationEntities
						try {
							locRepo.createMapLocations(createdTourEntity.getId(), createdLocationEntities);
						} catch (APIException e) {  // fails create location
							if (!createdTourEntity.getId().isEmpty()) { // is needed remove de created tourEntity
								//								mapRepo.remove(createdTourEntity);
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
					TourEntity updatedTourEntity = mapRepo.update(createdTourEntity);
					if (!createdLocationEntities.isEmpty()) {  // there are locationEntities
						try {
							locRepo.updateMapLocations(createdTourEntity.getId(), createdLocationEntities);
							createdTourEntity = updatedTourEntity;
						} catch (APIException e) {  // fails create location
							if (!updatedTourEntity.getId().isEmpty()) { // is needed revert the changes
								mapRepo.update(updatedTourEntity);
							}
							setError(e);
							m.postValue(null);
						}
					} else {
						createdTourEntity = updatedTourEntity;
					}
				} catch (APIException e) {  // fails create tourEntity
					setError(e);
					m.postValue(null);
					return;
				}
			}
			m.postValue(createdTourEntity);
		}).start();
		return m;
	}

	public List<PlaceEntity> getCreatedLocationEntities() {
		if (createdLocationEntities == null) createdLocationEntities = new ArrayList<>();
		return createdLocationEntities;
	}

	public void setCreatedLocationEntities(List<PlaceEntity> createdLocationEntities) {
		this.createdLocationEntities = createdLocationEntities;
	}

	public TourEntity getCreatedTourEntity() {
		if (createdTourEntity == null) createdTourEntity = new TourEntity();
		return createdTourEntity;
	}

	public void setCreatedTourEntity(TourEntity createdTourEntity) {
		this.createdTourEntity = createdTourEntity;
	}

	public void setLocation(String name, String description, Float lat, Float lon, int position) {
		PlaceEntity l = new PlaceEntity();
		l.setName(name);
		l.setDescription(description);
		l.setLat(lat);
		l.setLon(lon);
		l.setOrder(position);
		if (createdLocationEntities == null) {
			createdLocationEntities = new ArrayList<>();
		}

		if (position < createdLocationEntities.size()) {
			createdLocationEntities.set(position, l);
		} else {
			createdLocationEntities.add(l);
		}
	}

	public void setCreatedMap(String name, String description, Integer creator_id, PlayLevel pl) {
		if (createdTourEntity == null) {
			createdTourEntity = new TourEntity();
		}
		createdTourEntity.setName(name);
		createdTourEntity.setDescription(description);
		createdTourEntity.setCreator_id(creator_id);
		createdTourEntity.setMin_level(pl);
	}

	public MutableLiveData<TourEntity> getMap(String map_id) {
		MutableLiveData<TourEntity> m = new MutableLiveData<>();
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

	public MutableLiveData<List<PlaceEntity>> getLocations(String map_id) {
		MutableLiveData<List<PlaceEntity>> locs = new MutableLiveData<>();
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
		createdTourEntity = null;
		createdLocationEntities = null;
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
