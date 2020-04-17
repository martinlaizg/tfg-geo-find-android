package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.*
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "plays", foreignKeys = [ForeignKey(entity = Tour::class, parentColumns = "id", childColumns = "tour_id", onDelete = ForeignKey.CASCADE), ForeignKey(entity = User::class, parentColumns = "id", childColumns = "user_id", onDelete = ForeignKey.CASCADE)], indices = [Index(["tour_id", "user_id"]), Index("user_id")])
class Play {
    @PrimaryKey
    val id: Int
    var tour_id: Int
    var user_id: Int
    var created_at: Date? = null
    var updated_at: Date? = null
    private var updated: Date? = null

    @Ignore
    var tour: Tour? = null
        set(tour) {
            tour_id = tour.getId()
            field = tour
        }

    @Ignore
    var user: User? = null
        set(user) {
            user_id = user.getId()
            field = user
        }

    @Ignore
    var places: List<Place?>? = null

    constructor(id: Int, tourId: Int, userId: Int, createdAt: Date?,
                updatedAt: Date?, updated: Date?) {
        this.id = id
        this.tour_id = tourId
        this.user_id = userId
        this.created_at = createdAt
        this.updated_at = updatedAt
        this.updated = updated
    }

    @Ignore
    constructor(tourId: Int, userId: Int) {
        id = 0
        this.tour_id = tourId
        this.user_id = userId
        places = ArrayList()
    }

    val isOutOfDate: Boolean
        get() = DateUtils.isDateExpire(updated)

    fun getUpdated(): Date {
        return Date(Calendar.getInstance().time.time)
    }

    fun setUpdated(updated: Date?) {
        if (updated == null) {
            this.updated = Date(Calendar.getInstance().time.time)
        }
        this.updated = updated
    }

    val isCompleted: Boolean
        get() = places!!.size == tour.getPlaces().size

}