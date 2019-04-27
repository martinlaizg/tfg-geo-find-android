package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.PlayService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;

import java.util.List;

public class PlayRepository {

	private final PlayDAO playDAO;
	private final PlayService playService;
	private final TourRepository tourRepo;
	private final UserRepository userRepo;
	private final PlaceRepository placeRepo;
	private PlayRepository playRepo;

	PlayRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		playDAO = database.playDAO();
		playService = PlayService.getInstance();

		tourRepo = RepositoryFactory.getTourRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
		placeRepo = RepositoryFactory.getPlaceRepository(application);
	}

	public Play getPlay(int user_id, int tour_id) throws APIException {
		Play p = playDAO.getPlay(user_id, tour_id);
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
			p = playDAO.getPlay(user_id, tour_id);
		} else {
			p.setTour(tourRepo.getTour(tour_id));
			p.setUser(userRepo.getUser(user_id));
			p.setPlaces(placeRepo.getPlacesByPlay(p.getId()));
		}
		return p;
	}

	private List<Place> getPlacesByPlay(Integer play_id) {
		return playDAO.getPlacesByPlay(play_id);
	}

}
