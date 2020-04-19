package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "place_play",
		primaryKeys = ["play_id", "place_id"],
		foreignKeys = [
			ForeignKey(entity = Play::class, parentColumns = ["id"], childColumns = ["play_id"], onDelete = ForeignKey.CASCADE),
			ForeignKey(entity = Place::class, parentColumns = ["id"], childColumns = ["place_id"], onDelete = ForeignKey.CASCADE)],
		indices = [Index("place_id", "play_id")])
class PlacePlay(var placeId: Int, var playId: Int) {

	var createdAt: Date = Date(Calendar.getInstance().timeInMillis)
	var updatedAt: Date = Date(Calendar.getInstance().timeInMillis)

	private var updated: Date? = null
		get() = Date(Calendar.getInstance().timeInMillis)
		set(value) {
			field = value ?: Date(Calendar.getInstance().timeInMillis)
		}

	val isOutOfDate: Boolean
		get() = DateUtils.isDateExpire(updated)
}