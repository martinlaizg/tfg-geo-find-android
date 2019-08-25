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

	private static final String TAG = PlayRepository.class.getSimpleName();
	private static PlayRepository instance;

	private final PlayDAO playDAO;
	private final PlayService playService;

	private final TourRepository tourRepo;
	private final UserRepository userRepo;

	private final PlacePlayDAO placePlayDAO;

	private PlayRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		playDAO = database.playDAO();
		placePlayDAO = database.playPlaceDAO();

		playService = PlayService.getInstance(application);

		tourRepo = TourRepository.getInstance(application);
		userRepo = UserRepository.getInstance(application);
	}

	public static PlayRepository getInstance(Application application) {
		if(instance == null) instance = new PlayRepository(application);
		return instance;
	}

	/**
	 * Get the play between user and tour
	 *
	 * @param user_id
	 * 		the id of the user
	 * @param tour_id
	 * 		the id of the tour
	 * @return the play if exists, otherwise null
	 * @throws APIException
	 * 		exception from API
	 */
	public Play getPlay(int user_id, int tour_id) throws APIException {
		// Get from database
		Play p = playDAO.getPlay(user_id, tour_id);
		if(p != null) {
			// Check out of date
			if(p.isOutOfDate()) {
				playDAO.delete(p);
			} else {                    // If not retrieve data
				p.setTour(tourRepo.getTour(tour_id));
				p.setUser(userRepo.getUser(user_id));
				p.setPlaces(playDAO.getPlaces(p.getId()));
				return p;
			}
		}
		// Get from server
		try {
			p = playService.getUserPlay(user_id, tour_id);
			p.setUser_id(p.getUser().getId());
			p.setTour_id(p.getTour().getId());
			insert(p);
		} catch(APIException e) {
			p = null;
		}
		if(p == null) {
			p = new Play(tour_id, user_id);
			p.setTour(tourRepo.getTour(tour_id));
		}
		return p;
	}

	/**
	 * Insert the play into the local database
	 * Usually used for Play retrieved from server
	 *
	 * @param play
	 * 		Play to be inserted
	 */
	private void insert(Play play) {
		if(play != null) {
			userRepo.insert(play.getUser());
			tourRepo.insert(play.getTour());
			Play p = playDAO.getPlay(play.getId());
			if(p == null) {
				playDAO.insert(play);
			} else {
				playDAO.update(play);
			}
			for(Place place : play.getPlaces()) {
				PlacePlay pp = new PlacePlay(place.getId(), play.getId());
				placePlayDAO.insert(pp);
			}
		}
	}

	/**
	 * Complete a place of a play, creating the relation between both
	 *
	 * @param play_id
	 * 		play to complete
	 * @param place_id
	 * 		place to complete
	 * @return the new play
	 * @throws APIException
	 * 		exception from API
	 */
	public Play completePlace(Integer play_id, Integer place_id) throws APIException {
		Play p = playService.createPlacePlay(play_id, place_id);
		PlacePlay pp = new PlacePlay(place_id, play_id);
		placePlayDAO.insert(pp);
		return p;
	}

	/**
	 * Create the play and insert into the database
	 *
	 * @param user_id
	 * 		the id of the user who plays
	 * @param tour_id
	 * 		the id of the tour to play
	 * @return the play between user and tour
	 * @throws APIException
	 * 		exception from API
	 */
	public Play createPlay(int user_id, int tour_id) throws APIException {
		Play p = playService.createUserPlay(user_id, tour_id);
		insert(p);
		return p;
	}

	/**
	 * Get the list of plays of users
	 *
	 * @param user_id
	 * 		the id of the user
	 * @return the list of plays
	 * @throws APIException
	 * 		the exception from the database
	 */
	public List<Play> getUserPlays(int user_id) throws APIException {
		List<Play> plays = playDAO.getUserPlays(user_id);
		// Remove out of date
		for(int i = 0; i < plays.size(); i++) {
			if(plays.get(i).isOutOfDate()) {
				playDAO.delete(plays.remove(i));
				i--;
			}
		}
		if(plays.isEmpty()) {
			plays.addAll(playService.getUserPlays(user_id));
			for(Play p : plays) {
				userRepo.insert(p.getUser());
				tourRepo.insert(p.getTour());
				userRepo.insert(p.getTour().getCreator());
				insert(p);
			}
		} else {
			for(int i = 0; i < plays.size(); i++) {
				plays.get(i).setTour(tourRepo.getTour(plays.get(i).getTour_id()));
				plays.get(i).setPlaces(placePlayDAO.getPlayPlace(plays.get(i).getId()));
			}
		}
		return plays;
	}

	/**
	 * Refresh the list of plays of the user
	 * From the server into the database
	 *
	 * @param user_id
	 * 		the id of the user
	 * @throws APIException
	 * 		exception from the server
	 */
	private void refreshPlays(int user_id) throws APIException {
		List<Play> plays = playService.getUserPlays(user_id);
		for(Play p : plays) {
			insert(p);
		}
	}
}
