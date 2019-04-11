package com.martinlaizg.geofind.data.access.database.entities.relations;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TourCreatorPlaces {

	@Embedded
	private Tour tour;

	private String username;

	@Relation(parentColumn = "id", entityColumn = "tour_id")
	private List<Place> places;

	public Tour getTour() {
		return tour;
	}

	public void setTour(Tour tour) {
		this.tour = tour;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

}
