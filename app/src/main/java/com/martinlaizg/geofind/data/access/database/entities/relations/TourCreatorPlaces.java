package com.martinlaizg.geofind.data.access.database.entities.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.util.List;

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

	public List<Place> getPlaces() {
		return places;
	}

	public void setPlaces(List<Place> places) {
		this.places = places;
	}

	public User getCreator() {
		User u = new User();
		u.setUsername(getUsername());
		return u;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
}
