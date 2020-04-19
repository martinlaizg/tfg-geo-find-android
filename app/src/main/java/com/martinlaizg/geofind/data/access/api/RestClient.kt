package com.martinlaizg.geofind.data.access.api

import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.access.database.entities.User
import retrofit2.Call
import retrofit2.http.*

interface RestClient {
	/**
	 * Get the entire list of [Tour]
	 *
	 * @param params
	 * the params to get tours
	 * @return the list of tours
	 */
	@GET("tours")
	fun getTours(@QueryMap params: Map<String, String>): Call<MutableList<Tour>>

	/**
	 * Create the tour
	 *
	 * @param tour
	 * the tour to create
	 * @return the created tour
	 */
	@POST("tours")
	fun createTour(@Body tour: Tour): Call<Tour>

	/**
	 * Get a single tour
	 *
	 * @param tourId
	 * the id of the tour
	 * @return the tour
	 */
	@GET("tours/{tour_id}")
	fun getTour(@Path("tour_id") tourId: Int): Call<Tour>

	/**
	 * Update the tour with the `tour_id` with the data in `tour`
	 *
	 * @param tourId
	 * the id of the tour to update
	 * @param tour
	 * the new data of the tour
	 * @return the tour updated
	 */
	@PUT("tours/{tour_id}")
	fun update(@Path("tour_id") tourId: Int, @Body tour: Tour): Call<Tour>

	/**
	 * Get the [Play] by `tour_id` and `user_id`
	 *
	 * @param tourId
	 * the id of the tour
	 * @param userId
	 * the id of the user
	 * @return the play
	 */
	@GET("tours/{tour_id}/users/{user_id}/play")
	fun getUserPlay(@Path("tour_id") tourId: Int, @Path("user_id") userId: Int): Call<Play>

	/**
	 * Create the empty [Play] by `tour_id` and `user_id`
	 *
	 * @param tourId
	 * the id of the tour
	 * @param userId
	 * the id of the user
	 * @return the play
	 */
	@POST("tours/{tour_id}/users/{user_id}/play")
	fun createUserPlay(@Path("tour_id") tourId: Int, @Path("user_id") userId: Int): Call<Play>

	/**
	 * Log in the user
	 *
	 * @param login
	 * login data
	 * @return the logged user
	 */
	@POST("login")
	fun login(@Body login: Login): Call<User>

	/**
	 * Registry the user
	 *
	 * @param login
	 * the data to registry
	 * @return the new [User]
	 */
	@POST("users")
	fun registry(@Body login: Login): Call<User>

	/**
	 * Create the play of a place
	 *
	 * @param playId
	 * the id of the play
	 * @param placeId
	 * the id of the place
	 * @return the new [Play] with the place
	 */
	@POST("plays/{play_id}/places/{place_id}")
	fun createPlacePlay(@Path("play_id") playId: Int,
	                    @Path("place_id") placeId: Int): Call<Play>

	/**
	 * Send a message to support
	 *
	 * @param params
	 * the data (title and message)
	 * @return nothing
	 */
	@POST("support")
	fun sendSupportMessage(@QueryMap params: Map<String, String>): Call<Void>

	/**
	 * Update the data of the user
	 *
	 * @param userId
	 * the id of the user to update
	 * @param login
	 * the new data of the user and credentials
	 * @return the new user
	 */
	@PUT("users/{user_id}")
	fun updateUser(@Path("user_id") userId: Int, @Body login: Login): Call<User>

	/**
	 * Get the list of plays of the user
	 *
	 * @param userId
	 * the id of the user
	 * @return the list of plays
	 */
	@GET("users/{user_id}/plays")
	fun getUserPlays(@Path("user_id") userId: Int): Call<List<Play>>
}