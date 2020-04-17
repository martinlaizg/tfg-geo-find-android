package com.martinlaizg.geofind.data.access.database.entities

import android.location.Location
import androidx.room.*
import com.google.android.gms.maps.model.LatLng
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "places", foreignKeys = [ForeignKey(entity = Tour::class, parentColumns = "id", childColumns = "tour_id", onDelete = ForeignKey.CASCADE)], indices = [Index("tour_id")])
class Place {
	@PrimaryKey
	val id: Int
	private var name: String
	var lat: Double? = null
	var lon: Double? = null
	var tour_id: Int? = null
	private var description: String
	var order: Int? = null
	var created_at: Date? = null
	var updated_at: Date? = null
	private var updated: Date? = null

	// Question
	var question: String? = null
	var answer: String? = null
	var answer2: String? = null
	var answer3: String? = null
	var image: String? = null

	constructor(id: Int, name: String, lat: Double?, lon: Double?, tourId: Int?,
	            description: String, order: Int?, createdAt: Date?, updatedAt: Date?, updated: Date?) {
		this.id = id
		this.name = name
		this.lat = lat
		this.lon = lon
		this.tour_id = tourId
		this.description = description
		this.order = order
		this.created_at = createdAt
		this.updated_at = updatedAt
		this.updated = updated
	}

	@Ignore
	constructor() {
		id = 0
		name = ""
		description = ""
	}

	val position: LatLng?
		get() = if (lat == null || lon == null) null else LatLng(lat!!, lon!!)

	fun setPosition(position: LatLng) {
		lat = position.latitude
		lon = position.longitude
	}

	val isValid: Boolean
		get() = getName() != null && !getName()!!.isEmpty() && getDescription() != null &&
				!getDescription()!!.isEmpty() && lat != null && lon != null

	fun getName(): String? {
		return name
	}

	fun setName(name: String) {
		this.name = name
	}

	fun getDescription(): String? {
		return description
	}

	fun setDescription(description: String) {
		this.description = description
	}

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

	var location: Location?
		get() {
			if (lat == null || lon == null) return null
			val l = Location("")
			l.latitude = lat!!
			l.longitude = lon!!
			return l
		}
		set(location) {
			if (location == null) return
			lat = location.latitude
			lon = location.longitude
		}

	fun setCompleteQuestion(question: String?, correctAnswer: String?, secondAnswer: String?,
	                        thirdAnswer: String?) {
		this.question = question
		answer = correctAnswer
		answer2 = secondAnswer
		answer3 = thirdAnswer
	}
}