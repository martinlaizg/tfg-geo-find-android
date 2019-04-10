package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;

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
	Call<List<TourEntity>> getMaps(@QueryMap java.util.Map<String, String> params);

	@POST("maps")
	Call<TourEntity> createMap(@Body TourEntity tourEntity);

	// Get single tourEntity
	@GET("maps/{id}")
	Call<TourEntity> getMap(@Path("id") String id);

	// Update tourEntity
	@PUT("maps/{id}")
	Call<TourEntity> update(@Path("id") String id, @Body TourEntity m);

	// Get tourEntity locationEntities
	@GET("maps/{id}/locationEntities")
	Call<List<PlaceEntity>> getMapLocations(@Path("id") String map_id);

	// Create tourEntity locationEntities
	@POST("maps/{id}/locationEntities")
	Call<List<PlaceEntity>> createMapLocations(@Path("id") String map_id, @Body List<PlaceEntity> locationEntities);

	// Create tourEntity locationEntities
	@PUT("maps/{id}/locationEntities")
	Call<List<PlaceEntity>> updateMapLocations(@Path("id") String map_id, @Body List<PlaceEntity> locationEntities);

	// Get tourEntity location
	@GET("maps/{map_id}/locationEntities/{loc_id}")
	Call<List<PlaceEntity>> getMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id);

	// Update tourEntity placeEntity
	@PUT("maps/{map_id}/locationEntities/{loc_id}")
	Call<List<PlaceEntity>> updateMapLocation(@Path("map_id") String map_id, @Path("loc_id") String loc_id, @Body PlaceEntity placeEntity);


	//
	//
	//
	//
	// Locations requests

	// Get locationEntities
	@GET("locationEntities")
	Call<List<PlaceEntity>> getLocations(@QueryMap java.util.Map<String, String> params);

	// Get location
	@GET("locationEntities/{loc_id}")
	Call<PlaceEntity> getLocation(@Path("loc_id") String loc_id);


	//
	//
	//
	//
	// UserEntity requests

	// Login user
	@POST("login")
	Call<UserEntity> login(@Body UserEntity body);

	// Get all users
	@GET("users")
	Call<List<UserEntity>> getUsers();

	// Create a userEntity
	@POST("users")
	Call<UserEntity> registry(@Body UserEntity userEntity);

	// Get single user
	@GET("users/{usr_id}")
	Call<UserEntity> getUser(@Path("usr_id") String usr_id);
}
