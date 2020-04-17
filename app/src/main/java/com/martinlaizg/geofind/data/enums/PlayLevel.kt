package com.martinlaizg.geofind.data.enums

enum class PlayLevel {
	MAP, COMPASS, THERMOMETER;

	companion object {
		fun getPlayLevel(level: Int): PlayLevel {
			return values()[level]
		}
	}
}