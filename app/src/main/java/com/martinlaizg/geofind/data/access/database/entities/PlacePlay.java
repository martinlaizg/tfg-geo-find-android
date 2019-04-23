package com.martinlaizg.geofind.data.access.database.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "place_play", primaryKeys = {"play_id", "place_id"},
        foreignKeys = {@ForeignKey(entity = Play.class, parentColumns = "id", childColumns = "play_id"), @ForeignKey(entity = Place.class, parentColumns = "id", childColumns = "place_id")})
public class PlacePlay {


	private Integer play_id;
	private Integer place_id;

	public PlacePlay(Integer play_id, Integer place_id) {
		this.play_id = play_id;
		this.place_id = place_id;
	}
}
