package com.martinlaizg.geofind.data.access.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

@Dao
public interface PlayDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Play play);

	@Delete
	void delete(Play play);

	@Update
	void update(Play play);

	@Query("SELECT * FROM plays WHERE tour_id = :tour_id AND user_id = :user_id")
	Play getPlay(int user_id, int tour_id);

	@Query("SELECT p.* FROM places p INNER JOIN place_play pp ON p.id=pp.place_id WHERE pp.play_id = :play_id")
	List<Place> getPlaces(Integer play_id);

	@Query("SELECT * from plays WHERE id = :play_id")
	Play getPlay(Integer play_id);
}