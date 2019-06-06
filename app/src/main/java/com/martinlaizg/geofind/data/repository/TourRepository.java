package com.martinlaizg.geofind.data.repository;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.entities.Tour;

import java.util.List;

public class TourRepository {

	private static final String TAG = TourRepository.class.getSimpleName();
	private final TourService tourService;
	private final TourDAO tourDAO;

	private final PlaceRepository placeRepo;
	private final UserRepository userRepo;

	TourRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		tourDAO = database.tourDAO();
		tourService = TourService.getInstance();

		placeRepo = RepositoryFactory.getPlaceRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	/**
	 * Get the list of tours from local and removes the outdated ones
	 * At the same time refresh the local tours with the server
	 *
	 * @return the list of elements
	 */
	public List<Tour> getAllTours() throws APIException {
		List<Tour> tours = tourDAO.getAll();
		for(int i = 0; i < tours.size(); i++) {
			if(tours.get(i).isOutOfDate()) {
				tours.remove(i);
				i--;
			} else {
				Tour t = tours.get(i);
				t.setCreator(userRepo.getUser(t.getCreator_id()));
				t.setPlaces(placeRepo.getTourPlaces(t.getId()));
				tours.set(i, t);
			}
		}

		// If the list of tours is empty wait for refresh
		if(tours.isEmpty()) {
			tours = refreshTours();
		} else {
			// start the refresh on new thread to do it in background
			new Thread(() -> {
				try {
					refreshTours();
				} catch(APIException e) {
					Log.e(TAG, "getAllTours: ", e);
				}
			}).start();
		}
		return tours;
	}

	/**
	 * Load tours from server and insert into de local database
	 *
	 * @throws APIException
	 * 		the server exception
	 */
	private List<Tour> refreshTours() throws APIException {
		List<Tour> tours = tourService.getAllTours();
		for(Tour t : tours) {
			insert(t);
		}
		return tours;
	}

	/**
	 * Insert a Tour to the local database
	 * Insert the User creator and the list of Place recursively
	 *
	 * @param tour
	 * 		Tour to insert
	 */
	public void insert(Tour tour) {
		if(tour != null) {
			userRepo.insert(tour.getCreator());
			Tour t = tourDAO.getTour(tour.getId());
			if(t == null) {
				tourDAO.insert(tour);
			} else {
				tourDAO.update(tour);
			}
			placeRepo.insert(tour.getPlaces());
		}
	}

	/**
	 * Refresh the tour from the server and insert into the local database
	 *
	 * @param tour
	 * 		the tour to refresh, return null on error
	 * @return the tour refreshed
	 */
	private Tour refresh(Tour tour) {
		int tour_id = tour.getId();
		try {
			tour = tourService.getTour(tour.getId());
			if(tour != null) {
				insert(tour);
			} else {
				tourDAO.delete(tour_id);
			}
			return tour;
		} catch(APIException e) {
			Log.e(TAG, "refresh: ", e);
		}
		return null;
	}

	/**
	 * Update the tour on server and local
	 * If the tour is removed from server, return null
	 *
	 * @param tour
	 * 		tour to update
	 * @return tour updated or null if no exist on server
	 * @throws APIException
	 * 		exception from server
	 */
	public Tour update(Tour tour) throws APIException {
		int tour_id = tour.getId();
		tour = tourService.update(tour);
		if(tour != null) {
			insert(tour);
		} else {
			tourDAO.delete(tour_id);
		}
		return tour;
	}

	/**
	 * Create a tour on server and insert into local database
	 *
	 * @param tour
	 * 		tour to insert
	 * @return inserted tour
	 * @throws APIException
	 * 		the exception from API
	 */
	public Tour create(Tour tour) throws APIException {
		tour = tourService.create(tour);
		if(tour != null) insert(tour);
		return tour;
	}

	/**
	 * Get the tour with this id
	 *
	 * @param id
	 * 		the tour id
	 * @return the tour
	 * @throws APIException
	 * 		the server exception
	 */
	public Tour getTour(Integer id) throws APIException {
		Tour t = tourDAO.getTour(id);
		if(t == null) {
			t = tourService.getTour(id);
			insert(t);
		} else {
			t.setCreator(userRepo.getUser(t.getCreator_id()));
			t.setPlaces(placeRepo.getTourPlaces(id));
		}
		return t;
	}
}
