package com.martinlaizg.geofind.data.access.database.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

public class PlayWithPlaces {

	@Embedded
	public Play play;

	@Relation(parentColumn = "id", entityColumn = "id")
	List<Place> places;

	public Play getPlay() {
		return play;
	}

	public void setPlay(Play play) {
		this.play = play;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}
}
