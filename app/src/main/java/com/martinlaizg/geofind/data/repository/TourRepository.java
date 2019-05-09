package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.TourPlacesDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;

import java.util.ArrayList;
import java.util.List;

public class TourRepository {

	private final TourService tourService;
	private final TourDAO tourDAO;
	private final TourPlacesDAO tourPlacesDAO;

	private final PlaceRepository placeRepo;
	private final UserRepository userRepo;

	public TourRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		tourDAO = database.tourDAO();
		tourService = TourService.getInstance();
		tourPlacesDAO = database.tourPlacesDAO();

		placeRepo = RepositoryFactory.getPlaceRepository(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	public List<TourCreatorPlaces> getAllTours() throws APIException {
		List<TourCreatorPlaces> mls = tourPlacesDAO.getTourCreatorPlaces();

		if(mls != null) {
			for(int i = 0; i < mls.size(); i++) {
				if(mls.get(i).getTour().isOutOfDate()) {
					if(!updateTour(mls.get(i).getTour())) i--;
				}
			}
		}

		if(mls == null || mls.isEmpty()) {
			mls = new ArrayList<>();
			List<Tour> tours = tourService.getAllTours();
			if(tours != null) {
				for(Tour t : tours) {
					insert(t);
					TourCreatorPlaces tcp = new TourCreatorPlaces();
					tcp.setTour(t);
					tcp.setUsername(t.getCreator().getUsername());
					tcp.setPlaces(t.getPlaces());
					mls.add(tcp);
				}
			}
		}
		return mls;
	}

	private boolean updateTour(Tour tour) {
		int tour_id = tour.getId();
		try {
			tour = tourService.update(tour);
			return true;
		} catch(APIException e) {
			tourDAO.delete(tour_id);
		}
		return false;
	}

	public void insert(Tour t) {
		if(t != null) {
			userRepo.insert(t.getCreator());
			tourDAO.insert(t);
			placeRepo.insert(t.getPlaces());
		}
	}

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

	public Tour create(Tour tour) throws APIException {
		tour = tourService.create(tour);
		if(tour != null) tourDAO.insert(tour);
		return tour;
	}

	public Tour update(Tour tour) throws APIException {
		tour = tourService.update(tour);
		if(tour != null) {
			tourDAO.update(tour);
			if(tour.getPlaces() != null) {
				placeRepo.removeTourPlaces(tour.getId());
				for(Place p : tour.getPlaces()) placeRepo.insert(p);
			}
		}
		return tour;
	}

	public void getToursOnStart(int userId) {
		// TODO
	}
}
