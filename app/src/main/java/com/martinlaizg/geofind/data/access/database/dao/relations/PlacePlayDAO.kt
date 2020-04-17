package com.martinlaizg.geofind.data.access.database.dao.relations

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay

@Dao
interface PlacePlayDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(placePlay: PlacePlay?)

    @Query("SELECT p.* FROM places p INNER JOIN place_play pp ON p.id = pp.placeId WHERE pp.playId = :playId")
    fun getPlayPlace(playId: Int?): List<Place?>?
}