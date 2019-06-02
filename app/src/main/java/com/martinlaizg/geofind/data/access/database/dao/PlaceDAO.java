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

	@Update
	void update(Place place);

	@Query("SELECT * FROM places WHERE id = :placeId")
	Place getPlace(int placeId);

	@Query("SELECT * FROM places WHERE tour_id = :tour_id")
	List<Place> getTourPlaces(Integer tour_id);
}