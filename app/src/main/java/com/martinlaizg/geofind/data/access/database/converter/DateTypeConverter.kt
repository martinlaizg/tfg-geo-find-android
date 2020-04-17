package com.martinlaizg.geofind.data.access.database.converter

import androidx.room.TypeConverter
import java.sql.Date

object DateTypeConverter {
    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}