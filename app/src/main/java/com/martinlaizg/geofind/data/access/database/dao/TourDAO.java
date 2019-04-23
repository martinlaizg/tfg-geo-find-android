package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.Tour;

@Dao
public interface TourDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Tour tour);

	@Update
	void update(Tour tour);

	@Query("SELECT * FROM tours WHERE id = :tour_id")
	Tour getTour(Integer tour_id);
}
