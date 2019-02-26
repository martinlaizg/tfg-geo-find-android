package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entity.relation.MapAllLocations;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface UserLocationDAO {
    @Query("SELECT * from maps")
    List<MapAllLocations> loadUserAndPets();
}
