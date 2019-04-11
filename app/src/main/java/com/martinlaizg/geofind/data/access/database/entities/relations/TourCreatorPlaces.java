package com.martinlaizg.geofind.data.access.database.entities.relations;

import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class MapUserLocation {

	@Embedded
	private TourEntity tour;

	@Embedded(prefix = "c_")
	private UserEntity creator;

	@Relation(parentColumn = "id", entityColumn = "tour_id", entity = PlaceEntity.class)
	private List<PlaceEntity> locationEntities;

	public TourEntity getTour() {
		return tour;
	}

	public void setTour(TourEntity tour) {
		this.tour = tour;
	}

	public UserEntity getCreator() {
		return creator;
	}

	public void setCreator(UserEntity creator) {
		this.creator = creator;
	}

	public List<PlaceEntity> getLocationEntities() {
		return locationEntities;
	}

	public void setLocationEntities(List<PlaceEntity> locationEntities) {
		this.locationEntities = locationEntities;
	}
}
