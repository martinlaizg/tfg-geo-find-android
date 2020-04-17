package com.martinlaizg.geofind.utils

import java.sql.Date
import java.util.*

object DateUtils {
    const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
    private const val TIME_TO_EXPIRE = 15 * 60 * 1000 // 15min * 60s * 1000ms
            .toLong()

    fun isDateExpire(fromDate: Date?): Boolean {
        return Calendar.getInstance().time.time - fromDate!!.time > TIME_TO_EXPIRE
    }
}