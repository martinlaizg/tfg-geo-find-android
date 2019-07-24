package com.martinlaizg.geofind.data.access.api.service;

import android.app.Application;

public class ServiceFactory {

	private static UserService userService;
	private static TourService tourService;
	private static PlayService playService;

	public static UserService getUserService(Application application) {
		if(userService == null) {
			userService = new UserService();
			userService.instantiate(application);
		}
		return userService;
	}

	public static TourService getTourService(Application application) {
		if(tourService == null) {
			tourService = new TourService();
			tourService.instantiate(application);
		}
		return tourService;
	}

	public static PlayService getPlayService(Application application) {
		if(playService == null) {
			playService = new PlayService();
			playService.instantiate(application);
		}
		return playService;
	}
}
