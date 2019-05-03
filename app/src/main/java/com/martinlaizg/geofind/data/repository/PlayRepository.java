package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.PlayService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

public class PlayRepository {

	private final PlayDAO playDAO;
	private final PlayService playService;
	private final TourRepository tourRepo;
	private final UserRepository userRepo;
	private final PlaceRepository placeRepo;
	private final PlacePlayDAO placePlayDAO;

	PlayRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		playDAO = database.playDAO();
		playService = PlayService.getInstance();

		placePlayDAO = database.playPlaceDAO();

		tourRepo = RepositoryFactory.getTourRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
		placeRepo = RepositoryFactory.getPlaceRepository(application);
	}

	public Play getPlay(int user_id, int tour_id) throws APIException {
		Play p = getPlayDAO(user_id, tour_id);
		if(p == null) {
			p = playService.getUserPlay(user_id, tour_id);
			if(p == null) {
				p = playService.createUserPlay(user_id, tour_id);
			}
			p.setUser_id(p.getUser().getId());
			p.setTour_id(p.getTour().getId());
			tourRepo.insert(p.getTour());
			userRepo.insert(p.getUser());
			playDAO.insert(p);
			placeRepo.insert(p.getPlaces());
		}
		return p;
	}

	private Play getPlayDAO(int user_id, int tour_id) throws APIException {
		Play p = playDAO.getPlay(user_id, tour_id);
		if(p == null) {
			return null;
		}
		if(p.isOutOfDate()) {
			playDAO.delete(p);
			return null;
		}
		p.setTour(tourRepo.getTour(tour_id));
		p.setUser(userRepo.getUser(user_id));
		p.setPlaces(getPlacesByPlay(p.getId()));
		return p;
	}

	private List<Place> getPlacesByPlay(Integer play_id) {
		return playDAO.getPlaces(play_id);
	}

	public Play completePlay(Integer play_id, Integer place_id) throws APIException {
		Play p = playService.createPlacePlay(play_id, place_id);
		PlacePlay pp = new PlacePlay(place_id, play_id);
		placePlayDAO.insert(pp);
		return p;
	}
}
