package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "place_play", primaryKeys = ["play_id", "place_id"], foreignKeys = [ForeignKey(entity = Play::class, parentColumns = "id", childColumns = "play_id", onDelete = ForeignKey.CASCADE), ForeignKey(entity = Place::class, parentColumns = "id", childColumns = "place_id", onDelete = ForeignKey.CASCADE)], indices = [Index(["place_id", "play_id"])])
class PlacePlay(var placeId: Int, var playId: Int) {
    var created_at: Date
    var updated_at: Date
    private var updated: Date? = null

    fun getUpdated(): Date {
        return Date(Calendar.getInstance().time.time)
    }

    fun setUpdated(updated: Date?) {
        if (updated == null) {
            this.updated = Date(Calendar.getInstance().time.time)
        }
        this.updated = updated
    }

    val isOutOfDate: Boolean
        get() = DateUtils.isDateExpire(updated)

    init {
        created_at = Date(Calendar.getInstance().time.time)
        updated_at = Date(Calendar.getInstance().time.time)
    }
}