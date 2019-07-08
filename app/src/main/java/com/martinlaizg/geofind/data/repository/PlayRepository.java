package com.martinlaizg.geofind.data.repository;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.PlayService;
import com.martinlaizg.geofind.data.access.api.service.ServiceFactory;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.PlacePlayDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;

public class PlayRepository {

	private static final String TAG = PlayRepository.class.getSimpleName();

	private static PlayDAO playDAO;
	private static PlayService playService;

	private static TourRepository tourRepo;
	private static UserRepository userRepo;

	private static PlacePlayDAO placePlayDAO;

	void instantiate(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		playDAO = database.playDAO();
		playService = ServiceFactory.getPlayService(application);

		placePlayDAO = database.playPlaceDAO();

		tourRepo = RepositoryFactory.getTourRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
		PlaceRepository placeRepo = RepositoryFactory.getPlaceRepository(application);
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
		Play p = null;//playDAO.getPlay(user_id, tour_id);         // Get the play from the database
		if(p != null) {
			if(p.isOutOfDate()) {       // If is out of date remove from database
				playDAO.delete(p);
				p = null;
			} else {                    // If not retrieve data
				p.setTour(tourRepo.getTour(tour_id));
				p.setUser(userRepo.getUser(user_id));
				p.setPlaces(playDAO.getPlaces(p.getId()));
			}
		}
		if(p == null) {                 // The play no exist on database of is out of date
			try {
				p = playService.getUserPlay(user_id, tour_id);  // Get from server
			} catch(APIException e) {
				// If no exist
				if(e.getType() == ErrorType.EXIST) {
					Log.i(TAG, "getPlay: The play do not exist");
					try {
						// Create the play
						p = playService.createUserPlay(user_id, tour_id);
					} catch(APIException ex) {
						Log.e(TAG, "getPlay: ", ex);
						return null;
					}
				} else {
					Log.e(TAG, "getPlay: Other error");
					throw e;
				}
			}
			if(p == null) return null;
			p.setUser_id(p.getUser().getId());
			p.setTour_id(p.getTour().getId());
			insert(p);
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
	public Play completePlay(Integer play_id, Integer place_id) throws APIException {
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

}
