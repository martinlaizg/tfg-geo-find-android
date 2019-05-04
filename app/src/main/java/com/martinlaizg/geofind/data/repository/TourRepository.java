package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.TourDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.TourPlacesDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
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

	public List<Tour> getAllTours() throws APIException {
		List<TourCreatorPlaces> mls = tourPlacesDAO.getTourCreatorPlaces();
		List<Tour> ts = new ArrayList<>();
		for(TourCreatorPlaces ml : mls) {
			Tour t = ml.getTour();
			User u = new User();
			u.setUsername(ml.getUsername());
			t.setCreator(u);
			ts.add(t);
		}

		if(ts.isEmpty()) {
			ts = tourService.getAllTours();
			if(ts != null) {
				for(Tour t : ts) {
					tourDAO.insert(t);
					userRepo.insert(t.getCreator());
					for(Place p : t.getPlaces()) {
						placeRepo.insert(p);
					}
				}
			}
		}
		return ts;
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

	public void insert(Tour tour) {
		tourDAO.insert(tour);
	}
}
