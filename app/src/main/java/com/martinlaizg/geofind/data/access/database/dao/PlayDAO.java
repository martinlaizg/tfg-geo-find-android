package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

@Dao
public interface PlayPlacesDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Play play);

	@Query("SELECT * FROM plays WHERE id = :play_id")
	Play getPlay(Integer play_id);

	@Query("SELECT * FROM plays WHERE user_id = :user_id")
	List<Play> getPlay(Integer user_id, Integer tour_id);

}