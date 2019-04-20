package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entities.Tour;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface TourDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Tour tour);

	@Update
	void update(Tour tour);

}
