package com.martinlaizg.geofind.data.access.database.converter;

import com.martinlaizg.geofind.data.access.database.entity.enums.UserType;

import androidx.room.TypeConverter;

public class UserTypeTypeConverter {


	@TypeConverter
	public static UserType toPlayLevel(String userTypeString) {
		return userTypeString == null ?
				null :
				UserType.valueOf(userTypeString);
	}

	@TypeConverter
	public static String fromPlayLevel(UserType userType) {
		return userType.toString();
	}
}
