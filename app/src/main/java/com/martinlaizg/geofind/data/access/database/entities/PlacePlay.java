package com.martinlaizg.geofind.data.access.database.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

import org.jetbrains.annotations.NotNull;

@Entity(tableName = "place_play", primaryKeys = {"play_id", "place_id"},
        foreignKeys = {@ForeignKey(entity = Play.class, parentColumns = "id", childColumns = "play_id"), @ForeignKey(entity = Place.class, parentColumns = "id", childColumns = "place_id")},
indices = @Index({"place_id","play_id"}))
public class PlacePlay {

	@NonNull
	private Integer play_id;
	@NonNull
	private Integer place_id;

	public PlacePlay(@NotNull Integer play_id, @NotNull Integer place_id) {
		this.play_id = play_id;
		this.place_id = place_id;
	}

	@NotNull
	public Integer getPlay_id() {
		return play_id;
	}

	public void setPlay_id(@NotNull Integer play_id) {
		this.play_id = play_id;
	}

	@NotNull
	public Integer getPlace_id() {
		return place_id;
	}

	public void setPlace_id(@NotNull Integer place_id) {
		this.place_id = place_id;
	}
}
