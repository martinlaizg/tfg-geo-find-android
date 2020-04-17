package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.data.enums.UserType
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "users", indices = [Index(["id"])])
class User {
	@PrimaryKey
	val id: Int
	var email: String? = null
	var username: String? = null
	var name: String? = null
	var image: String? = null
	var user_type: UserType? = null
	var created_at: Date? = null
	var updated_at: Date? = null
	private var updated: Date? = null

	@Ignore
	var secure: String? = null

	@Ignore
	private val createdPlaces: List<Place>? = null

	constructor(id: Int, email: String?, username: String?, name: String?, userType: UserType?,
	            createdAt: Date?, updatedAt: Date?) {
		this.id = id
		this.email = email
		this.username = username
		this.name = name
		this.user_type = userType
		this.created_at = createdAt
		this.updated_at = updatedAt
	}

	@Ignore
	constructor() {
		id = 0
	}

	fun toJson(): String {
		val gson = GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create()
		return gson.toJson(this)
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

}