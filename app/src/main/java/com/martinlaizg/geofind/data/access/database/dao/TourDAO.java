package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.util.List;

@Dao
public interface TourDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Tour tour);

	@Update
	void update(Tour tour);

	@Query("SELECT * FROM tours WHERE id = :tour_id")
	Tour getTour(Integer tour_id);

	@Query("DELETE FROM tours WHERE id = :tour_id")
	void delete(int tour_id);

	@Query("SELECT * FROM tours")
	List<Tour> getAll();
}
