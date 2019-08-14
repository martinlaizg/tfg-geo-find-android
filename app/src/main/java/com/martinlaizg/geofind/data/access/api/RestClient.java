package com.martinlaizg.geofind.data.access.api;

import com.martinlaizg.geofind.data.access.api.entities.Login;
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

	/**
	 * Get the entire list of {@link Tour}
	 *
	 * @param params
	 * 		the params to get tours
	 * @return the list of tours
	 */
	@GET("tours")
	Call<List<Tour>> getTours(@QueryMap java.util.Map<String, String> params);

	/**
	 * Create the tour
	 *
	 * @param tour
	 * 		the tour to create
	 * @return the created tour
	 */
	@POST("tours")
	Call<Tour> createTour(@Body Tour tour);

	/**
	 * Get a single tour
	 *
	 * @param tour_id
	 * 		the id of the tour
	 * @return the tour
	 */
	@GET("tours/{tour_id}")
	Call<Tour> getTour(@Path("tour_id") Integer tour_id);

	/**
	 * Update the tour with the {@code tour_id} with the data in {@code tour}
	 *
	 * @param tour_id
	 * 		the id of the tour to update
	 * @param tour
	 * 		the new data of the tour
	 * @return the tour updated
	 */
	@PUT("tours/{tour_id}")
	Call<Tour> update(@Path("tour_id") Integer tour_id, @Body Tour tour);

	/**
	 * Get the {@link Play} by {@code tour_id} and {@code user_id}
	 *
	 * @param tour_id
	 * 		the id of the tour
	 * @param user_id
	 * 		the id of the user
	 * @return the play
	 */
	@GET("tours/{tour_id}/users/{user_id}/play")
	Call<Play> getUserPlay(@Path("tour_id") Integer tour_id, @Path("user_id") Integer user_id);

	/**
	 * Create the empty {@link Play} by {@code tour_id} and {@code user_id}
	 *
	 * @param tour_id
	 * 		the id of the tour
	 * @param user_id
	 * 		the id of the user
	 * @return the play
	 */
	@POST("tours/{tour_id}/users/{user_id}/play")
	Call<Play> createUserPlay(@Path("tour_id") Integer tour_id, @Path("user_id") Integer user_id);

	/**
	 * Log in the user
	 *
	 * @param login
	 * 		login data
	 * @return the logged user
	 */
	@POST("login")
	Call<User> login(@Body Login login);

	/**
	 * Registry the user
	 *
	 * @param login
	 * 		the data to registry
	 * @return the new {@link User}
	 */
	@POST("users")
	Call<User> registry(@Body Login login);

	/**
	 * Create the play of a place
	 *
	 * @param play_id
	 * 		the id of the play
	 * @param place_id
	 * 		the id of the place
	 * @return the new {@link Play} with the place
	 */
	@POST("plays/{play_id}/places/{place_id}")
	Call<Play> createPlacePlay(@Path("play_id") Integer play_id,
			@Path("place_id") Integer place_id);

	/**
	 * Send a message to support
	 *
	 * @param params
	 * 		the data (title and message)
	 * @return nothing
	 */
	@POST("support")
	Call<Void> sendSupportMessage(@QueryMap Map<String, String> params);

	/**
	 * Update the data of the user
	 *
	 * @param user_id
	 * 		the id of the user to update
	 * @param login
	 * 		the new data of the user and credentials
	 * @return the new user
	 */
	@PUT("users/{user_id}")
	Call<User> updateUser(@Path("user_id") Integer user_id, @Body Login login);
}
