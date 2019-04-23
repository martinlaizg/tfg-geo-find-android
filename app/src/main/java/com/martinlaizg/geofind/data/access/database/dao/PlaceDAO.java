package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

@Dao
public interface PlaceDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Place place);

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(List<Place> place);

	@Update
	void update(Place place);

	@Query("SELECT * FROM places WHERE tour_id = :place_id")
	Place getPlace(Integer place_id);

	@Query("SELECT * FROM places WHERE tour_id = :tour_id ORDER BY `order`")
	List<Place> getPlacesByTour(Integer tour_id);

	@Query("DELETE FROM places WHERE tour_id = :tour_id")
	void removeTourPlaces(Integer tour_id);
}