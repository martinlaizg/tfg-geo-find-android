package com.martinlaizg.geofind.data.repository;

import android.app.Application;
import android.util.Log;

import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.TourPlacesDAO;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;

import java.util.ArrayList;
import java.util.List;

public class TourRepository {

	private static final String TAG = TourRepository.class.getSimpleName();
	private final TourService tourService;
	private final TourDAO tourDAO;
	private final TourPlacesDAO tourPlacesDAO;

	private final PlaceRepository placeRepo;
	private final UserRepository userRepo;

	TourRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		tourDAO = database.tourDAO();
		tourService = TourService.getInstance();
		tourPlacesDAO = database.tourPlacesDAO();

		placeRepo = RepositoryFactory.getPlaceRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	/**
	 * Get the TourCreatorPlaces list from local and update each item from server
	 *
	 * @return the list of elements
	 */
	public List<TourCreatorPlaces> getAllTours() {
		List<TourCreatorPlaces> tcps = tourPlacesDAO.getTourCreatorPlaces();
		List<Tour> tours = tourDAO.getAll();
		if(tcps != null) {
			for(int i = 0; i < tcps.size(); i++) {
				tcps.get(i).getTour().setPlaces(tcps.get(i).getPlaces());
				if(tcps.get(i).getTour().isOutOfDate()) {
					Tour newTour = refresh(tcps.get(i).getTour());
					if(newTour == null) {
						tcps.remove(i);
						i--;
						continue;
					}
					insert(newTour);
					TourCreatorPlaces tcp = new TourCreatorPlaces();
					tcp.setTour(newTour);
					tcp.setPlaces(newTour.getPlaces());
					tcp.setUsername(newTour.getCreator().getUsername());
					tcps.set(i, tcp);
				}
			}
		}
		return tcps;
	}

	/**
	 * Refresh the tour from the server and insert into the local database
	 *
	 * @param tour
	 * 		the tour to refresh, return null on error
	 * @return the tour refreshed
	 */
	private Tour refresh(Tour tour) {
		try {
			tour = tourService.getTour(tour.getId());
			if(tour != null) {
				insert(tour);
			}
			return tour;
		} catch(APIException e) {
			Log.e(TAG, "refresh: ", e);
		}
		return null;
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
			tourDAO.insert(tour);
			placeRepo.insert(tour.getPlaces());
		}
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
		TourCreatorPlaces tcp = tourPlacesDAO.getTour(id);
		Tour t;
		if(tcp == null) {
			t = tourService.getTour(id);
		} else {
			t = tcp.getTour();
			t.setCreator(tcp.getCreator());
			t.setPlaces(tcp.getPlaces());
		}
		return t;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public void getToursOnStart(int userId) {
		// TODO
	}

	/**
	 * Load tours from server and insert into de local database
	 *
	 * @return the list of {@link TourCreatorPlaces} from server
	 * @throws APIException
	 * 		the server exception
	 */
	public List<TourCreatorPlaces> refreshTours() throws APIException {

		List<TourCreatorPlaces> tcps = new ArrayList<>();
		List<Tour> tours = tourService.getAllTours();
		for(Tour t : tours) {
			insert(t);
			TourCreatorPlaces tcp = new TourCreatorPlaces();
			tcp.setTour(t);
			tcp.setPlaces(t.getPlaces());
			tcp.setUsername(t.getCreator().getUsername());
			tcps.add(tcp);
		}
		return tcps;
	}
}
