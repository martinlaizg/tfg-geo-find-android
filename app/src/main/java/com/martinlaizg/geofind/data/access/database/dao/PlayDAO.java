package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

@Dao
public interface PlayDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Play play);


	@Query("SELECT * FROM plays WHERE tour_id = :tour_id AND user_id = :user_id")
	Play getPlay(int tour_id, int user_id);

	@Query("SELECT * FROM plays WHERE")
	List<Place> getPlacesByPlay(Integer play_id);
}