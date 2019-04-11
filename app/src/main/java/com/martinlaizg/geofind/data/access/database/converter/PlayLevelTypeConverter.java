package com.martinlaizg.geofind.data.access.database.converter;

import com.martinlaizg.geofind.data.enums.PlayLevel;

import androidx.room.TypeConverter;

public class PlayLevelTypeConverter {


	@TypeConverter
	public static PlayLevel toPlayLevel(String playLevelString) {
		return playLevelString == null ?
				null :
				playLevelString.isEmpty() ?
						PlayLevel.MAP :
						PlayLevel.valueOf(playLevelString);
	}

	@TypeConverter
	public static String fromPlayLevel(PlayLevel playLevel) {
		return playLevel == null ?
				"" :
				playLevel.toString();
	}
}
