package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.relations.PlayWithPlaces;

@Dao
public interface PlacePlayDAO {

	@Insert
	void insert(PlacePlay placePlay);

	@Transaction
	@Query("SELECT * FROM plays WHERE id = :play_id")
	PlayWithPlaces getPlayWithPlaces(int play_id);

}
