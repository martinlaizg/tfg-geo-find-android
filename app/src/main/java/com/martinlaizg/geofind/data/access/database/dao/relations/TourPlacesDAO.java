package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Transaction;

import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;

import java.util.List;

@Dao
public interface TourPlacesDAO {

	@Transaction
	@Query("SELECT t.*, username FROM tours as t LEFT JOIN users AS u ON u.id = t.creator_id;")
	List<TourCreatorPlaces> getTourCreatorPlaces();

	@Transaction
	@Query("SELECT t.*, username FROM tours as t LEFT JOIN users AS u ON u.id = t.creator_id WHERE t.id = :tour_id;")
	TourCreatorPlaces getTour(Integer tour_id);
}
