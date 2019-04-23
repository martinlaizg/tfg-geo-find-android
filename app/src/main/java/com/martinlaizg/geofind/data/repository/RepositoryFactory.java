package com.martinlaizg.geofind.data.repository;

import android.app.Application;

public class RepositoryFactory {
	private static UserRepository userRepository;
	private static TourRepository tourRepository;
	private static PlaceRepository placeRepository;
	private static PlayRepository playRepository;

	public static UserRepository getUserRepository(Application application) {
		if (userRepository == null) userRepository = new UserRepository(application);
		return userRepository;
	}

	public static TourRepository getTourRepository(Application application) {
		if (tourRepository == null) tourRepository = new TourRepository(application);
		return tourRepository;
	}

	public static PlaceRepository getPlaceRepository(Application application) {
		if (placeRepository == null) placeRepository = new PlaceRepository(application);
		return placeRepository;
	}

	public static PlayRepository getPlayRepository(Application application) {
		if (playRepository == null) playRepository = new PlayRepository(application);
		return playRepository;
	}
}
