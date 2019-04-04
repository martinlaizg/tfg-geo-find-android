package com.martinlaizg.geofind.data.access.retrofit;

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
	// Maps requests

	// Get maps with optional filter
	@GET("maps")
	Call<List<Map>> getMaps(@QueryMap java.util.Map<String, String> params);

	@POST("maps")
	Call<Map> createMap(@Body Map map);

	// Get single map
	@GET("maps/{id}")
	Call<Map> getMap(@Path("id") String id);

	// Get locations by map
	@GET("maps/{id}/locations")
	Call<List<Location>> getLocationsByMap(@Path("id") String map_id);

	// Update map
	@PUT("maps/{id}")
	Call<Map> update(@Path("id") String id, @Body Map m);


	//
	//
	//
	//
	// Locations requests

	// Get locations
	@GET("locations")
	Call<List<Location>> getLocations(@QueryMap java.util.Map<String, String> params);

	// Get single location
	@GET("locations/{id}")
	Call<Location> getLocation(@Path("id") String id);

	// Create a location
	@POST("locations")
	Call<Location> createLocation(@Body Location loc);


	//
	//
	//
	//
	// User requests

	// Login user
	@POST("login")
	Call<User> login(@Body User body);

	@POST("users")
	Call<List<User>> getUsers(@QueryMap java.util.Map<String, String> params);

	@POST("users")
	Call<User> registry(@Body User user);
}
