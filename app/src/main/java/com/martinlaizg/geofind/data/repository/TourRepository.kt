package com.martinlaizg.geofind.data.repository

import android.app.Application
import android.util.Log
import com.martinlaizg.geofind.data.access.api.service.TourService
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.AppDatabase
import com.martinlaizg.geofind.data.access.database.dao.TourDAO
import com.martinlaizg.geofind.data.access.database.entities.Tour

class TourRepository private constructor(application: Application) {

	private var tourService: TourService
	private var tourDAO: TourDAO
	private var placeRepo: PlaceRepository
	private var userRepo: UserRepository

	companion object {
		private val TAG = TourRepository::class.java.simpleName
		private var instance: TourRepository? = null

		@kotlin.jvm.JvmStatic
		fun getInstance(application: Application): TourRepository {
			return instance ?: synchronized(this) {
				val newInstance = TourRepository(application)
				instance = newInstance
				newInstance
			}
		}
	}

	init {
		tourDAO = AppDatabase.getDatabase(application).tourDAO()
		tourService = TourService.getInstance(application)
		placeRepo = PlaceRepository.getInstance(application)
		userRepo = UserRepository.getInstance(application)
	}

	/**
	 * Get the list of tours from local and removes the outdated ones
	 * At the same time refresh the local tours with the server data
	 *
	 * @return the list of elements
	 */
	@get:Throws(APIException::class)
	val allTours: MutableList<Tour>
		get() {
			var tours = tourDAO.all
			var i = 0
			while (i < tours.size) {
				if (tours[i].isOutOfDate) {
					tours.removeAt(i)
					i--
				} else {
					val t = tours[i]
					t.creator = userRepo.getUser(t.creator?.id ?: 0)
					t.places = placeRepo.getTourPlaces(t.id)
					tours[i] = t
				}
				i++
			}

			// If the list of tours is empty wait for refresh
			if (tours.isEmpty()) {
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
	private fun refreshTours(): MutableList<Tour> {
		val tours = tourService.allTours
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
			userRepo.insert(tour.creator)
			val t = tourDAO.getTour(tour.id)
			if (t == null) {
				tourDAO.insert(tour)
			} else {
				tourDAO.update(tour)
			}
			placeRepo.insert(tour.places)
		}
	}

	/**
	 * Update the tour on server and local
	 * If the tour is removed from server, return null
	 *
	 * @param newTour
	 * tour to update
	 * @return tour updated or null if no exist on server
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun update(newTour: Tour): Tour {
		var tour = newTour
		val tourId = tour.id
		val updatedTour = tourService.update(newTour)
		if (tour != null) {
			tourDAO.delete(tour.id)
			insert(tour)
		} else {
			tourDAO.delete(tourId)
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
	fun create(tour: Tour): Tour? {
		val createdTour = tourService.create(tour)
		createdTour?.let { insert(it) }
		return createdTour
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
	fun getTour(id: Int): Tour? {
		var t = tourDAO.getTour(id)
		if (t == null) {
			t = tourService.getTour(id)
			insert(t)
		} else {
			t.creator = userRepo.getUser(t.creatorId!!)
			t.places = placeRepo.getTourPlaces(id)
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
	fun getTours(stringQuery: String): MutableList<Tour> {
		val tours = tourService.getTours(stringQuery)
		for (t in tours!!) {
			insert(t)
		}
		return tours
	}
}