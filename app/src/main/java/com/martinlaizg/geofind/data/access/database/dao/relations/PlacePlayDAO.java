package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;

import java.util.List;

@Dao
public interface PlacePlayDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(PlacePlay placePlay);

	@Query("SELECT p.* FROM places p INNER JOIN place_play pp ON p.id = pp.place_id WHERE pp.play_id = :play_id")
	List<Place> getPlayPlace(Integer play_id);
}
