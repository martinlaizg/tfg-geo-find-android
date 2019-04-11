package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.database.entities.Place;
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

	// Get maps with optional filter
	@GET("maps")
	Call<List<Tour>> getMaps(@QueryMap java.util.Map<String, String> params);

	@POST("maps")
	Call<Tour> createMap(@Body Tour tour);

	// Get single tourEntity
	@GET("maps/{id}")
	Call<Tour> getMap(@Path("id") Integer id);

	// Update tourEntity
	@PUT("maps/{id}")
	Call<Tour> update(@Path("id") Integer id, @Body Tour m);

	// Get tourEntity locationEntities
	@GET("maps/{id}/locationEntities")
	Call<List<Place>> getMapLocations(@Path("id") Integer map_id);

	// Create tourEntity locationEntities
	@POST("maps/{id}/locationEntities")
	Call<List<Place>> createMapLocations(@Path("id") Integer map_id, @Body List<Place> locationEntities);

	// Create tourEntity locationEntities
	@PUT("maps/{id}/locationEntities")
	Call<List<Place>> updateMapLocations(@Path("id") Integer map_id, @Body List<Place> locationEntities);

	// Get tourEntity location
	@GET("maps/{map_id}/locationEntities/{loc_id}")
	Call<List<Place>> getMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id);

	// Update tourEntity place
	@PUT("maps/{map_id}/locationEntities/{loc_id}")
	Call<List<Place>> updateMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id, @Body Place place);


	//
	//
	//
	//
	// Locations requests

	// Get locationEntities
	@GET("locationEntities")
	Call<List<Place>> getLocations(@QueryMap java.util.Map<String, String> params);

	// Get location
	@GET("locationEntities/{loc_id}")
	Call<Place> getLocation(@Path("loc_id") Integer loc_id);


	//
	//
	//
	//
	// User requests

	// Login user
	@POST("login")
	Call<User> login(@Body User body);

	// Get all users
	@GET("users")
	Call<List<User>> getUsers();

	// Create a user
	@POST("users")
	Call<User> registry(@Body User user);

	// Get single user
	@GET("users/{usr_id}")
	Call<User> getUser(@Path("usr_id") String usr_id);
}
