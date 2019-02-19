package com.martinlaizg.geofind.dataAccess.database.converter;

import java.sql.Date;

import androidx.room.TypeConverter;

public class DateTypeConverter {
    @TypeConverter
    public static Date toDate(Long dateLong) {
        return dateLong == null ? null : new Date(dateLong);
    }

    @TypeConverter
    public static Long fromDate(Date date) {
        return date == null ? null : date.getTime();
    }
}
