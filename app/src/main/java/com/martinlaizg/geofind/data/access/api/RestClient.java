package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;

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
	Call<List<Map>> getMaps(@QueryMap java.util.Map<String, String> params);

	@POST("maps")
	Call<Map> createMap(@Body Map map);

	// Get single map
	@GET("maps/{id}")
	Call<Map> getMap(@Path("id") String id);

	// Update map
	@PUT("maps/{id}")
	Call<Map> update(@Path("id") String id, @Body Map m);

	// Get map locations
	@GET("maps/{id}/locations")
	Call<List<Location>> getMapLocations(@Path("id") String map_id);

	// Create map locations
	@POST("maps/{id}/locations")
	Call<List<Location>> createMapLocations(@Path("id") String map_id, @Body List<Location> locations);

	// Create map locations
	@PUT("maps/{id}/locations")
	Call<List<Location>> updateMapLocations(@Path("id") String map_id, @Body List<Location> locations);

	// Get map location
	@GET("maps/{map_id}/locations/{loc_id}")
	Call<List<Location>> getMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id);

	// Update map location
	@PUT("maps/{map_id}/locations/{loc_id}")
	Call<List<Location>> updateMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id, @Body Location location);


	//
	//
	//
	//
	// Locations requests

	// Get locations
	@GET("locations")
	Call<List<Location>> getLocations(@QueryMap java.util.Map<String, String> params);

	// Get location
	@GET("locations/{loc_id}")
	Call<Location> getLocation(@Path("loc_id") String loc_id);


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
