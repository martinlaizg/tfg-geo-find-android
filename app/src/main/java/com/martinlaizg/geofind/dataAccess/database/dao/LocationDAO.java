package com.martinlaizg.geofind.dataAccess.database.dao;

import com.martinlaizg.geofind.dataAccess.database.entity.Location;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface LocationDAO {

    @Insert
    void insert(Location location);

    @Update
    void update(Location location);

    @Delete
    void delete(Location location);

    @Query("SELECT * FROM locations")
    LiveData<List<Location>> getAllLocations();
}
