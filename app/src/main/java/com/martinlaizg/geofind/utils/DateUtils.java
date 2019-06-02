package com.martinlaizg.geofind.utils;

import java.sql.Date;
import java.util.Calendar;

public class DateUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final long TIME_TO_EXPIRE = 20 * 1000; // 20s

	public static boolean isDateExpire(Date fromDate) {
		return Calendar.getInstance().getTime().getTime() - fromDate.getTime() > TIME_TO_EXPIRE;
	}
}
