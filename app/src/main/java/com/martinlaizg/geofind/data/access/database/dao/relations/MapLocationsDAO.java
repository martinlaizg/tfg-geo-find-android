package com.martinlaizg.geofind.data.access.database.dao.relations;

import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

@Dao
public interface MapLocationsDAO {

	@Transaction
	@Query("SELECT t.*, username FROM tours as t LEFT JOIN users AS u ON u.user_id = t.creator_id;")
	List<TourCreatorPlaces> getTourCreatorPlaces();
}
