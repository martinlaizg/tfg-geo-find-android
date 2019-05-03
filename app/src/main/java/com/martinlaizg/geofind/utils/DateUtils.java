package com.martinlaizg.geofind.utils;

import java.sql.Date;
import java.util.Calendar;

public class DateUtils {

	public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
	public static final long TIME_TO_EXPIR = 1000;

	public static boolean isDateExpire(Date fromDate) {
		return Calendar.getInstance().getTime().getTime() - fromDate.getTime() > TIME_TO_EXPIR;
	}
}
