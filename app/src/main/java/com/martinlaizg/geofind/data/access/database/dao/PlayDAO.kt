package com.martinlaizg.geofind.data.access.database.dao

import androidx.room.*
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.Play

@Dao
interface PlayDAO {
	@Insert(onConflict = OnConflictStrategy.REPLACE)
	fun insert(play: Play?)

	@Delete
	fun delete(play: Play?)

	@Update
	fun update(play: Play?)

	@Query("SELECT * FROM plays WHERE tour_id = :tourId AND user_id = :userId")
	fun getPlay(userId: Int, tourId: Int): Play?

	@Query("SELECT p.* FROM places p INNER JOIN place_play pp ON p.id=pp.place_id WHERE pp" +
			".play_id = :play_id ORDER BY `order` ASC")
	fun getPlaces(playId: Int?): List<Place?>?

	@Query("SELECT * from plays WHERE id = :playId")
	fun getPlay(playId: Int?): Play?

	@Query("SELECT * FROM plays WHERE user_id = :userId")
	fun getUserPlays(userId: Int): MutableList<Play?>?
}