package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.*
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "plays",
		foreignKeys = [
			ForeignKey(entity = Tour::class, parentColumns = ["id"], childColumns = ["tour_id"], onDelete = ForeignKey.CASCADE),
			ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["user_id"], onDelete = ForeignKey.CASCADE)],
		indices = [Index("tour_id", "user_id"), Index("user_id")])
class Play {
	@PrimaryKey
	val id: Int
	var tourId: Int
	var userId: Int
	private var createdAt: Date = Date(Calendar.getInstance().timeInMillis)
	private var updatedAt: Date = Date(Calendar.getInstance().timeInMillis)

	private var updated: Date = Date(Calendar.getInstance().timeInMillis)
		get() = Date(Calendar.getInstance().timeInMillis)
		set

	@Ignore
	var tour: Tour? = null
		set(tour) {
			tourId = tour?.id ?: 0
			field = tour
		}

	@Ignore
	var user: User? = null
		set(user) {
			userId = user?.id!!
			field = user
		}

	@Ignore
	var places: List<Place> = ArrayList()

	constructor(id: Int, tourId: Int, userId: Int, createdAt: Date, updatedAt: Date, updated: Date) {
		this.id = id
		this.tourId = tourId
		this.userId = userId
		this.createdAt = createdAt
		this.updatedAt = updatedAt
		this.updated = updated
	}

	@Ignore
	constructor(tourId: Int, userId: Int) {
		id = 0
		this.tourId = tourId
		this.userId = userId
		places = ArrayList()
	}

	val isOutOfDate: Boolean
		get() = DateUtils.isDateExpire(updated)

	val isCompleted: Boolean
		get() {
			return if (tour?.places != null) {
				places.size == tour!!.places.size
			} else {
				false
			}
		}
}