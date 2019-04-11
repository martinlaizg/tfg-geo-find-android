package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Place place);

	@Update
	void update(Place place);

	@Query("SELECT * FROM places WHERE tour_id = :place_id")
	Place getLocation(Integer place_id);

	@Query("SELECT * FROM places WHERE tour_id = :tour_id ORDER BY `order`")
	List<Place> getPlacesByTour(Integer tour_id);
}