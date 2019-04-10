package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.TourEntity;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MapDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(TourEntity tourEntity);

	@Update
	void update(TourEntity tourEntity);

	@Query("SELECT * FROM tours WHERE id = :tour_id")
	TourEntity getMap(String tour_id);

}
