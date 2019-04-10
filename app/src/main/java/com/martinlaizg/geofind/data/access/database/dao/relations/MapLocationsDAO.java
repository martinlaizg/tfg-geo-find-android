package com.martinlaizg.geofind.data.access.database.dao.relations;

import com.martinlaizg.geofind.data.access.database.entities.relations.MapUsernameLocations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface MapLocationsDAO {

	@Query("SELECT t.*,u.username as c_username, u.id as c_id FROM tours as t left join users as u on u.id = t.creator_id;")
	List<MapUsernameLocations> getMapWithLocations();
}
