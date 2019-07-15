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

// TODO add JavaDoc
public interface RestClient {

	@GET("tours")
	Call<List<Tour>> getTours(@QueryMap java.util.Map<String, String> params);

	@POST("tours")
	Call<Tour> createTour(@Body Tour tour);

	@GET("tours/{tour_id}")
	Call<Tour> getTour(@Path("tour_id") Integer tour_id);

	@PUT("tours/{tour_id}")
	Call<Tour> update(@Path("tour_id") Integer tour_id, @Body Tour t);

	@GET("tours/{tour_id}/users/{user_id}/play")
	Call<Play> getUserPlay(@Path("tour_id") Integer tour_id, @Path("user_id") Integer user_id);

	@POST("login")
	Call<User> login(@Body Login login);

	@POST("users")
	Call<User> registry(@Body Login login);

	@POST("plays")
	Call<Play> createUserPlay(@QueryMap Map<String, String> params);

	@POST("plays/{play_id}/places")
	Call<Play> createPlacePlay(@Path("play_id") Integer play_id, @Body PlacePlay placePlay);

	@POST("support")
	Call<Void> sendSupportMessage(@QueryMap Map<String, String> params);

	@PUT("users/{user_id}")
	Call<User> updateUser(@Path("user_id") Integer user_id, @Body Login login);
}
