package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.util.List;

@Dao
public interface TourDAO {

	/**
	 * Insert a tour into the database
	 *
	 * @param tour
	 * 		the tour to be inserted
	 */
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Tour tour);

	/**
	 * Update a tour
	 *
	 * @param tour
	 * 		the tour to be updated
	 */
	@Update
	void update(Tour tour);

	/**
	 * Get a single tour by id
	 *
	 * @param tour_id
	 * 		the id of the tour to retrieve
	 * @return the tour
	 */
	@Query("SELECT * FROM tours WHERE id = :tour_id")
	Tour getTour(Integer tour_id);

	/**
	 * Delete a single tour by id
	 *
	 * @param tour_id
	 * 		the id of the tour to delete
	 */
	@Query("DELETE FROM tours WHERE id = :tour_id")
	void delete(int tour_id);

	/**
	 * Get all the tours
	 *
	 * @return the list of tours
	 */
	@Query("SELECT * FROM tours")
	List<Tour> getAll();
}
