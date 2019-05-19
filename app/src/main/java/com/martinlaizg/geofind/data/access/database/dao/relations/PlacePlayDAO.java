package com.martinlaizg.geofind.data.access.database.dao.relations;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.relations.PlayWithPlaces;

@Dao
public interface PlacePlayDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(PlacePlay placePlay);

}
