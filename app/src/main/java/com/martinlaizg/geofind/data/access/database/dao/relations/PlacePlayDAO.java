package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.relations.PlayWithPlaces;

import java.util.List;

@Dao
public interface PlacePlayDAO {

	@Insert
	void insert(PlacePlay placePlay);


	@Query("SELECT places.* FROM places INNER JOIN place_play as join_pp ON places.id=join_pp.place_id WHERE join_pp.play_id = :play_id")
	List<Place> getPlacesByPlay(int play_id);


	@Query("SELECT plays.* FROM plays INNER JOIN place_play as join_pp ON plays.id=join_pp.play_id WHERE join_pp.place_id = :place_id")
	List<Play> getPlaysByPlace(int place_id);


	@Transaction
	@Query("SELECT * FROM plays WHERE id = :play_id")
	PlayWithPlaces getPlayWithPlaces(int play_id);

}
