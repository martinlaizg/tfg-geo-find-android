package com.martinlaizg.geofind.data.access.database.converter

import androidx.room.TypeConverter
import com.martinlaizg.geofind.data.enums.UserType

object UserTypeTypeConverter {
    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun toPlayLevel(userTypeString: String?): UserType? {
        return if (userTypeString == null) null else UserType.valueOf(userTypeString)
    }

    @kotlin.jvm.JvmStatic
    @TypeConverter
    fun fromPlayLevel(userType: UserType?): String {
        return userType?.toString() ?: ""
    }
}