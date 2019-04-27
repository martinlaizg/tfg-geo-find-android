package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;

import java.util.List;

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

	// Get tour places
	@GET("tours/{tour_id}/palces")
	Call<List<Place>> getTourPlaces(@Path("tour_id") Integer tour_id);

	// Create tourEntity locationEntities
	@POST("tours/{tour_id}/places")
	Call<List<Place>> createTourPlaces(@Path("tour_id") Integer tour_id, @Body List<Place> places);

	// Create tourEntity locationEntities
	@PUT("tours/{tour_id}/places")
	Call<List<Place>> updateTourPlaces(@Path("tour_id") Integer tour_id, @Body List<Place> places);

	//
	//
	//
	//
	// Locations requests

	// Get locationEntities
	@GET("places")
	Call<List<Place>> getPlaces(@QueryMap java.util.Map<String, String> params);

	// Get location
	@GET("places/{loc_id}")
	Call<Place> getLocation(@Path("loc_id") Integer loc_id);

	//
	//
	//
	//
	// User requests

	// Login user
	@POST("login")
	Call<User> login(@Body User body);

	// Create a user
	@POST("users")
	Call<User> registry(@Body User user);

	// Get single user
	@GET("users/{user_id}")
	Call<User> getUser(@Path("user_id") String user_id);

	// Get user play
	@GET("users/{user_id}/tours/{tour_id}")
	Call<Play> getUserPlay(@Path("user_id") int user_id, @Path("tour_id") int tour_id);

	// Create user play
	@POST("users/{user_id}/tours/{tour_id}")
	Call<Play> createUserPlay(int user_id, int tour_id);
}
