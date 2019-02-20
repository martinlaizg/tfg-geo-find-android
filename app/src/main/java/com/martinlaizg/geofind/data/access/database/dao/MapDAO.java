package com.martinlaizg.geofind.data.access.database.dao;

import com.martinlaizg.geofind.data.access.database.entity.Map;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface MapDAO {

    @Insert
    void insert(Map map);

    @Update
    void update(Map map);

    @Delete
    void delete(Map map);

    @Query("SELECT * FROM maps")
    LiveData<List<Map>> getAllMaps();
}
