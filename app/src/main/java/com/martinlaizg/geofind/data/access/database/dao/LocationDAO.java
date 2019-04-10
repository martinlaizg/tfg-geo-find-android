package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(PlaceEntity placeEntity);

	@Update
	void update(PlaceEntity placeEntity);

	@Query("SELECT * FROM places WHERE id = :place_id")
	PlaceEntity getLocation(String place_id);

	@Query("SELECT * FROM places WHERE tour_id = :tour_id ORDER BY `order`")
	List<PlaceEntity> getPlacesByTour(String tour_id);
}