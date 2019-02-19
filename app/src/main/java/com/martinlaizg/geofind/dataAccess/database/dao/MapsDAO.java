package com.martinlaizg.geofind.dataAccess.database.dao;

import com.martinlaizg.geofind.dataAccess.database.entity.Maps;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MapsDAO {

    @Insert
    void insert(Maps maps);

    @Update
    void update(Maps maps);

    @Delete
    void delete(Maps maps);

    @Query("SELECT * FROM maps")
    LiveData<List<Maps>> getAllMaps();
}
