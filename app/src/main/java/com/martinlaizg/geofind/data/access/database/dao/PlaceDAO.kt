package com.martinlaizg.geofind.data.access.database.dao

import androidx.room.*
import com.martinlaizg.geofind.data.access.database.entities.Place

@Dao
interface PlaceDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(place: Place?)

    @Update
    fun update(place: Place?)

    @Query("SELECT * FROM places WHERE id = :placeId")
    fun getPlace(placeId: Int): Place?

    @Query("SELECT * FROM places WHERE tour_id = :tourId ORDER BY `order` ASC")
    fun getTourPlaces(tourId: Int?): MutableList<Place?>?
}