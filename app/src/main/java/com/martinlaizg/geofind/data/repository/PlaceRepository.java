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
	private final PlacePlayDAO placePlayDAO;

	PlaceRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		placeDAO = database.placeDAO();
		placePlayDAO = database.playPlaceDAO();
		playService = PlayService.getInstance();
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public void insert(List<Place> places) {
		for(Place p : places) insert(p);
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public void insert(Place place) {
		Place p = placeDAO.getPlace(place.getId());
		if(p == null) {
			placeDAO.insert(place);
		} else {
			placeDAO.update(place);
		}
	}

	public List<Place> getTourPlaces(Integer tour_id) {
		return placeDAO.getTourPlaces(tour_id);

	}
}
