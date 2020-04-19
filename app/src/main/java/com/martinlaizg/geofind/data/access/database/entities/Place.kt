package com.martinlaizg.geofind.data.access.database.entities

import android.location.Location
import androidx.room.*
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "places",
		foreignKeys = [ForeignKey(entity = Tour::class, parentColumns = ["id"], childColumns = ["tour_id"], onDelete = ForeignKey.CASCADE)],
		indices = [Index("tour_id")])
class Place {
	@PrimaryKey
	val id: Int
	var name: String
	var description: String

	private var lat: Double? = null
	private var lon: Double? = null
	private var tourId: Int? = null
	var order: Int? = null

	// Question
	var question: String? = null
	private var answer: String? = null
	private var answer2: String? = null
	private var answer3: String? = null
	private var image: String? = null

	constructor(id: Int, name: String, description: String) {
		this.id = id
		this.name = name
		this.description = description
	}

	@Ignore
	constructor() {
		id = 0
		name = ""
		description = ""
	}

	var position: LatLng?
		get() {
			return if (lat == null || lon == null) {
				null
			} else {
				LatLng(lat!!, lon!!)
			}
		}
		set(value) {
			lat = value?.latitude
			lon = value?.longitude
		}

	val isValid: Boolean
		get() = name.isNotEmpty() && description.isNotEmpty() && lat != null && lon != null

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

	fun fillQuestion(newQuestion: String = "", correctAnswer: String = "", secondAnswer: String = "", thirdAnswer: String = "") {
		question = newQuestion
		answer = correctAnswer
		answer2 = secondAnswer
		answer3 = thirdAnswer
	}
}