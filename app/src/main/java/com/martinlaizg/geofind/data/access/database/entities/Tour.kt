package com.martinlaizg.geofind.data.access.database.entities

import androidx.room.*
import com.martinlaizg.geofind.data.enums.PlayLevel
import com.martinlaizg.geofind.utils.DateUtils
import java.sql.Date
import java.util.*

@Entity(tableName = "tours", foreignKeys = [ForeignKey(entity = User::class, parentColumns = "id", childColumns = "creator_id", onDelete = ForeignKey.CASCADE, onUpdate = ForeignKey.CASCADE)], indices = [Index("creator_id")])
class Tour {
    @PrimaryKey
    val id: Int
    private var name: String
    private var description: String
    var image: String? = null
    private var min_level: PlayLevel
    var created_at: Date? = null
    var updated_at: Date? = null
    var creator_id: Int? = null
    private var updated: Date? = null

    @Ignore
    var places: MutableList<Place?>? = null

    @Ignore
    var creator: User? = null

    constructor(id: Int, name: String, description: String, minLevel: PlayLevel, createdAt: Date?,
                updatedAt: Date?, creatorId: Int?, updated: Date?) {
        this.id = id
        this.name = name
        this.description = description
        image = null
        this.min_level = minLevel
        this.created_at = createdAt
        this.updated_at = updatedAt
        this.creator_id = creatorId
        this.updated = updated
    }

    @Ignore
    constructor() {
        id = 0
        name = ""
        description = ""
        min_level = PlayLevel.MAP
        places = ArrayList()
    }//

    //
    val isValid: Boolean
        get() {
            for (p in places!!) {
                if (!p!!.isValid) return false
            }
            return getName() != null && !getName()!!.isEmpty() && //
                    getDescription() != null && !getDescription()!!.isEmpty() && //
                    getMinLevel() != null
        }

    fun getName(): String? {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    //-------------------------
    fun getDescription(): String? {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getMinLevel(): PlayLevel? {
        return min_level
    }

    fun setMinLevel(minLevel: PlayLevel) {
        this.min_level = minLevel
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