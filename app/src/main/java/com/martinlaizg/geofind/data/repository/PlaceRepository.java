package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.PlayService;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

public class PlaceRepository {

	private final PlaceDAO placeDAO;
	private final PlayService playService;
	private PlacePlayDAO placePlayDAO;

	PlaceRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		placeDAO = database.placeDAO();
		placePlayDAO = database.playPlaceDAO();
		playService = PlayService.getInstance();
	}

	public void insert(List<Place> places) {
		placeDAO.insert(places);
	}

	public void removeTourPlaces(Integer tourId) {
		placeDAO.removeTourPlaces(tourId);
	}

	public void insert(Place place) {
		placeDAO.insert(place);
	}

	public List<Place> getPlacesByPlay(Integer play_id) {
		return placePlayDAO.getPlayWithPlaces(play_id).getPlaces();
	}
}
