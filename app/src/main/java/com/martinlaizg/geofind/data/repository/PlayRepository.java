package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.PlayDAO;
import com.martinlaizg.geofind.data.access.database.entities.Play;

public class PlayRepository {

	private final PlayDAO playDAO;

	public PlayRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		playDAO = database.playDAO();
		//		locationService = LocationService.getInstance();
	}

	public Play getPlay(int tour_id, int user_id) {
		return playDAO.getPlay(tour_id, user_id);
	}
}
