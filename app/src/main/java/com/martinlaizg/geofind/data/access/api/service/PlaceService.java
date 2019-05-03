package com.martinlaizg.geofind.data.access.api.service;

import com.martinlaizg.geofind.data.access.api.RestClient;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;

public class PlaceService {

	private static PlaceService placeService;
	private static RestClient restClient;
	private final String TAG = PlaceService.class.getSimpleName();

	public static PlaceService getInstance() {
		if(restClient == null) {
			restClient = RetrofitInstance.getRestClient();
		}
		if(placeService == null) {
			placeService = new PlaceService();
		}
		return placeService;
	}

}
