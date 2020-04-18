package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.*
import com.martinlaizg.geofind.data.enums.PlayLevel
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*
import kotlin.collections.ArrayList

@Entity(tableName = "tours",
		foreignKeys = [ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["creator_id"], onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)],
		indices = [Index("creator_id")])
class Tour {
	@PrimaryKey
	val id: Int
	private var name: String
	private var description: String
	private var minLevel: PlayLevel
	private var image: String? = null
	private var createdAt: Date? = null
	private var updatedAt: Date? = null
	private var creatorId: Int? = null

	private var updated: Date? = null
		get() = Date(Calendar.getInstance().timeInMillis)
		set(value) {
			field = value ?: Date(Calendar.getInstance().timeInMillis)
		}

	@Ignore
	var places: MutableList<Place> = ArrayList()

	@Ignore
	var creator: User? = null

	constructor(id: Int, name: String, description: String, minLevel: PlayLevel, createdAt: Date?,
	            updatedAt: Date?, creatorId: Int?, updated: Date?) {
		this.id = id
		this.name = name
		this.description = description
		image = null
		this.minLevel = minLevel
		this.createdAt = createdAt
		this.updatedAt = updatedAt
		this.creatorId = creatorId
		this.updated = updated
	}

	@Ignore
	constructor() {
		id = 0
		name = ""
		description = ""
		minLevel = PlayLevel.MAP
		places = ArrayList()
	}

	val isValid: Boolean
		get() {
			for (p in places) {
				if (!p.isValid) return false
			}
			return name.isNotEmpty() && description.isNotEmpty()
		}

	val isOutOfDate: Boolean
		get() = DateUtils.isDateExpire(updated)

}