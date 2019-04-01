package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entity.Location;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDAO {

	@Insert(onConflict = OnConflictStrategy.REPLACE)
	void insert(Location location);

	@Update
	void update(Location location);

	@Delete
	void delete(Location location);

	@Query("DELETE FROM locations")
	void deleteAllLocations();

	@Query("SELECT * FROM locations")
	List<Location> getAllLocations();

	@Query("SELECT * FROM locations WHERE id = :loc_id")
	Location getLocation(String loc_id);

	@Query("SELECT * FROM locations WHERE map_id = :map_id")
	List<Location> getLocationsByMap(String map_id);
}