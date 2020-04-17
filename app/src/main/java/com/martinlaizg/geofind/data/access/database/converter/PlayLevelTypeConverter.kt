package com.martinlaizg.geofind.data.access.database.converter

import androidx.room.TypeConverter
import com.martinlaizg.geofind.data.enums.PlayLevel

object PlayLevelTypeConverter {
    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toPlayLevel(playLevelString: String?): PlayLevel? {
        return if (playLevelString == null) null else if (playLevelString.isEmpty()) PlayLevel.MAP else PlayLevel.valueOf(playLevelString)
    }

    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun fromPlayLevel(playLevel: PlayLevel?): String {
        return playLevel?.toString() ?: ""
    }
}