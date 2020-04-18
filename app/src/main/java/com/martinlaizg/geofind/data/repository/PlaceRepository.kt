package com.martinlaizg.geofind.data.repository

import android.app.Application
import com.martinlaizg.geofind.data.access.database.AppDatabase
import com.martinlaizg.geofind.data.access.database.dao.PlaceDAO
import com.martinlaizg.geofind.data.access.database.entities.Place

class PlaceRepository private constructor(application: Application) {

	private val tag = PlaceRepository::class.simpleName
	private val placeDAO: PlaceDAO = AppDatabase.getDatabase(application).placeDAO()

	companion object {
		private var instance: PlaceRepository? = null
		fun getInstance(application: Application): PlaceRepository {
			return instance ?: synchronized(this) {
				val newInstance = PlaceRepository(application)
				instance = newInstance
				newInstance
			}
		}
	}

	/**
	 * Insert a List of Place into the database
	 *
	 * @param places
	 * places to be inserted
	 */
	fun insert(places: List<Place>) {
		for (p in places) insert(p)
	}

	/**
	 * Insert a single Place into the database
	 *
	 * @param place
	 * place to be inserted
	 */
	private fun insert(place: Place) {
		val p = placeDAO.getPlace(place.id)
		if (p == null) {
			placeDAO.insert(place)
		} else {
			placeDAO.update(place)
		}
	}

	/**
	 * Get the list of places, by tour id, from the database
	 *
	 * @param tourId
	 * tour id of the places
	 * @return the list of places
	 */
	fun getTourPlaces(tourId: Int?): MutableList<Place?>? {
		return placeDAO.getTourPlaces(tourId)
	}
}