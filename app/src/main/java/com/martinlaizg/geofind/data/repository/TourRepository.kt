package com.martinlaizg.geofind.data.repository

import android.app.Application
import android.util.Log
import com.martinlaizg.geofind.data.access.api.service.TourService
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.AppDatabase
import com.martinlaizg.geofind.data.access.database.dao.TourDAO
import com.martinlaizg.geofind.data.access.database.entities.Tour

class TourRepository private constructor(application: Application) {// start the refresh in background// If the list of tours is empty wait for refresh
	/**
	 * Get the list of tours from local and removes the outdated ones
	 * At the same time refresh the local tours with the server
	 *
	 * @return the list of elements
	 */
	@get:Throws(APIException::class)
	val allTours: MutableList<Tour?>?
		get() {
			var tours = tourDAO.getAll()
			var i = 0
			while (i < tours!!.size) {
				if (tours!![i]!!.isOutOfDate) {
					tours!!.removeAt(i)
					i--
				} else {
					val t = tours!![i]
					t.creator = userRepo!!.getUser(t.creator_id)
					t.places = placeRepo!!.getTourPlaces(t.id)
					tours!![i] = t
				}
				i++
			}

			// If the list of tours is empty wait for refresh
			if (tours!!.isEmpty()) {
				tours = refreshTours()
			} else {
				// start the refresh in background
				Thread(Runnable {
					try {
						refreshTours()
					} catch (e: APIException) {
						Log.e(TAG, "getAllTours: ", e)
					}
				}).start()
			}
			return tours
		}

	/**
	 * Load tours from server and insert into de local database
	 *
	 * @throws APIException
	 * the server exception
	 */
	@Throws(APIException::class)
	private fun refreshTours(): MutableList<Tour?>? {
		val tours = tourService.getAllTours()
		for (t in tours!!) {
			insert(t)
		}
		return tours
	}

	/**
	 * Insert a Tour to the local database
	 * Insert the User creator and the list of Place recursively
	 *
	 * @param tour
	 * Tour to insert
	 */
	fun insert(tour: Tour?) {
		if (tour != null) {
			userRepo!!.insert(tour.creator)
			val t = tourDAO!!.getTour(tour.id)
			if (t == null) {
				tourDAO!!.insert(tour)
			} else {
				tourDAO!!.update(tour)
			}
			placeRepo!!.insert(tour.places)
		}
	}

	/**
	 * Update the tour on server and local
	 * If the tour is removed from server, return null
	 *
	 * @param tour
	 * tour to update
	 * @return tour updated or null if no exist on server
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun update(tour: Tour?): Tour? {
		var tour = tour
		val tourId = tour.getId()
		tour = tourService!!.update(tour)
		if (tour != null) {
			tourDAO!!.delete(tour.id)
			insert(tour)
		} else {
			tourDAO!!.delete(tourId)
		}
		return tour
	}

	/**
	 * Create a tour on server and insert into local database
	 *
	 * @param tour
	 * tour to insert
	 * @return inserted tour
	 * @throws APIException
	 * the exception from API
	 */
	@Throws(APIException::class)
	fun create(tour: Tour?): Tour? {
		var tour = tour
		tour = tourService!!.create(tour)
		tour?.let { insert(it) }
		return tour
	}

	/**
	 * Get the tour with this id
	 *
	 * @param id
	 * the tour id
	 * @return the tour
	 * @throws APIException
	 * the server exception
	 */
	@Throws(APIException::class)
	fun getTour(id: Int?): Tour? {
		var t = tourDAO!!.getTour(id)
		if (t == null) {
			t = tourService!!.getTour(id)
			insert(t)
		} else {
			t.creator = userRepo!!.getUser(t.creator_id)
			t.places = placeRepo!!.getTourPlaces(id)
		}
		return t
	}

	/**
	 * Get tours from de server with the stringQuery and insert into the database
	 *
	 * @param stringQuery
	 * the string
	 * @return the list of tours
	 */
	@Throws(APIException::class)
	fun getTours(stringQuery: String?): MutableList<Tour?>? {
		val tours = tourService!!.getTours(stringQuery)
		for (t in tours!!) {
			insert(t)
		}
		return tours
	}

	companion object {
		private val TAG = TourRepository::class.java.simpleName
		private var instance: TourRepository? = null
		private var tourService: TourService?
		private var tourDAO: TourDAO?
		private var placeRepo: PlaceRepository?
		private var userRepo: UserRepository?

		@kotlin.jvm.JvmStatic
		fun getInstance(application: Application): TourRepository? {
			if (instance == null) instance = TourRepository(application)
			return instance
		}
	}

	init {
		val database: AppDatabase = AppDatabase.Companion.getDatabase(application)
		tourDAO = database.tourDAO()
		tourService = TourService.Companion.getInstance(application)
		placeRepo = PlaceRepository.Companion.getInstance(application)
		userRepo = UserRepository.Companion.getInstance(application)
	}
}