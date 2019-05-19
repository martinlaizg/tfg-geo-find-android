package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.database.entities.PlacePlay;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RestClient {

	//
	//
	//
	//
	// Tours

	// Get tours with optional filter
	@GET("tours")
	Call<List<Tour>> getTours(@QueryMap java.util.Map<String, String> params);

	@POST("tours")
	Call<Tour> createTour(@Body Tour tour);

	// Get single tourEntity
	@GET("tours/{tour_id}")
	Call<Tour> getTour(@Path("tour_id") Integer tour_id);

	// Update tourEntity
	@PUT("tours/{tour_id}")
	Call<Tour> update(@Path("tour_id") Integer tour_id, @Body Tour t);

	//
	//
	//
	//
	// User requests

	// Login user
	@POST("login")
	Call<User> login(@Body Login login);

	// Create a user
	@POST("users")
	Call<User> registry(@Body Login login);

	//
	//
	//
	//
	// Play requests

	// Get user play
	@GET("plays")
	Call<Play> getUserPlay(@QueryMap Map<String, String> params);

	// Create user play
	@POST("plays")
	Call<Play> createUserPlay(@QueryMap Map<String, String> params);

	@POST("plays/{play_id}/places")
	Call<Play> createPlacePlay(@Path("play_id") Integer play_id, @Body PlacePlay placePlay);

	@POST("support")
	Call<Void> sendSupportMessage(@QueryMap Map<String, String> params);
}
