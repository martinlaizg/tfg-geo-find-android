package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MapDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Map map);

    @Update
    void update(Map map);

    @Delete
    void delete(Map map);

    @Query("SELECT * FROM maps")
    List<Map> getAllMaps();

    @Query("DELETE FROM maps")
    void deleteAllMaps();

    @Query("SELECT * FROM maps WHERE id = :mapId")
    Map getMap(String mapId);

    @Query("SELECT * FROM locations WHERE map_id = :map_id")
    List<Location> getLocations(String map_id);
}
