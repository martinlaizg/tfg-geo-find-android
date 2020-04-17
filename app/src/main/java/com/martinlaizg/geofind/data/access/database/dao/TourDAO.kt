package com.martinlaizg.geofind.data.access.database.dao

import androidx.room.*
import com.martinlaizg.geofind.data.access.database.entities.Tour

@Dao
interface TourDAO {
    /**
     * Insert a tour into the database
     *
     * @param tour
     * the tour to be inserted
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(tour: Tour?)

    /**
     * Update a tour
     *
     * @param tour
     * the tour to be updated
     */
    @Update
    fun update(tour: Tour?)

    /**
     * Get a single tour by id
     *
     * @param tourId
     * the id of the tour to retrieve
     * @return the tour
     */
    @Query("SELECT * FROM tours WHERE id = :tourId")
    fun getTour(tourId: Int?): Tour?

    /**
     * Delete a single tour by id
     *
     * @param tourId
     * the id of the tour to delete
     */
    @Query("DELETE FROM tours WHERE id = :tourId")
    fun delete(tourId: Int)

    /**
     * Get all the tours
     *
     * @return the list of tours
     */
    @get:Query("SELECT * FROM tours")
    val all: MutableList<Tour?>?
}