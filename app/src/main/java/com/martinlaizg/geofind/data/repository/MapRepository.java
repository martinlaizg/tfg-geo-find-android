package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.MapService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.LocationDAO;
import com.martinlaizg.geofind.data.access.database.dao.MapDAO;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.dao.relations.MapLocationsDAO;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.access.database.entities.relations.TourCreatorPlaces;

import java.util.ArrayList;
import java.util.List;

public class MapRepository {

	private final MapService mapService;
	private final MapDAO mapDAO;
	private final MapLocationsDAO mapLocsDAO;
	private final LocationDAO locDAO;
	private final UserDAO userDAO;

	public MapRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		mapDAO = database.mapDAO();
		locDAO = database.locationDAO();
		userDAO = database.userDAO();
		mapLocsDAO = database.mapLocsDAO();
		mapService = MapService.getInstance();
	}

	public List<Tour> getAllMaps() throws APIException {
		List<TourCreatorPlaces> mls = mapLocsDAO.getTourCreatorPlaces();
		List<Tour> ts = new ArrayList<>();
		for (TourCreatorPlaces ml : mls) {
			Tour t = ml.getTour();
			User u = new User();
			u.setUsername(ml.getUsername());
			t.setCreator(u);
			ts.add(t);
		}

		if (ts.isEmpty()) {
			ts = mapService.getAllMaps();
			if (ts != null) {
				for (Tour t : ts) {
					mapDAO.insert(t);
					userDAO.insert(t.getCreator());
					for (Place l : t.getPlaces()) {
						locDAO.insert(l);
					}
				}
			}
		}
		return ts;
	}

	public Tour getMap(Integer id) throws APIException {
		Tour m = parseEntityToTour(mapDAO.getMap(id));
		if (m == null) {
			m = mapService.getMap(id);
		}
		return m;
	}

	private Tour parseEntityToTour(Tour tour) {
		return (Tour) tour;
	}

	public Tour create(Tour tourEntity) throws APIException {
		tourEntity = mapService.create(tourEntity);
		if (tourEntity != null) mapDAO.insert(tourEntity);
		return tourEntity;
	}

	public Tour update(Tour tourEntity) throws APIException {
		tourEntity = mapService.update(tourEntity);
		if (tourEntity != null) mapDAO.update(tourEntity);
		return tourEntity;
	}
}
