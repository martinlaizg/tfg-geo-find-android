package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.enums.PlayLevel
import com.martinlaizg.geofind.data.repository.TourRepository

class CreatorViewModel(application: Application) : AndroidViewModel(application) {

	private val tourRepo: TourRepository?

	var storedTour: Tour? = null
		private set
	var error: ErrorType? = null
		private set
	var place: Place? = null

	fun createTour(): MutableLiveData<Tour?> {
		val m = MutableLiveData<Tour?>()
		Thread(Runnable {
			if (storedTour!!.id == 0) { // Create tour
				try {
					storedTour = tourRepo!!.create(storedTour!!)
				} catch (e: APIException) {
					error = e.type
					m.postValue(null)
					return@Runnable
				}
			} else { // Update tour
				try {
					storedTour = tourRepo!!.update(storedTour!!)
				} catch (e: APIException) {
					error = e.type
					m.postValue(null)
					return@Runnable
				}
			}
			m.postValue(storedTour)
		}).start()
		return m
	}

	fun updateTour(name: String, description: String, creatorId: Int?, pl: PlayLevel, imageUrl: String?) {
		if (storedTour == null) storedTour = Tour()
		storedTour!!.name = name
		storedTour!!.description = description
		storedTour!!.creatorId = creatorId
		storedTour!!.minLevel = pl
		storedTour!!.image = imageUrl
	}

	/**
	 * Get the tour by the id
	 * If this.tour has the same id, return this.tour
	 * Else load from server `loadTour(int`
	 *
	 * @param tourId
	 * the id of the tour
	 * @return the tour
	 */
	fun getTour(tourId: Int): MutableLiveData<Tour?> {
		val t = MutableLiveData<Tour?>()
		if (storedTour == null || storedTour!!.id != tourId) {
			return loadTour(tourId)
		}
		Thread(Runnable { t.postValue(storedTour) }).start()
		return t
	}

	/**
	 * Get the tour with the tour_id passed by parameter from the server
	 * If the id is 0, return a new Tour
	 *
	 * @param tourId
	 * the id of the tour
	 * @return the tour
	 */
	private fun loadTour(tourId: Int): MutableLiveData<Tour?> {
		val t = MutableLiveData<Tour?>()
		Thread(Runnable {
			storedTour = Tour()
			if (tourId > 0) {
				try {
					storedTour = tourRepo!!.getTour(tourId)
				} catch (e: APIException) {
					error = e.type
					storedTour = null
				}
			}
			t.postValue(storedTour)
		}).start()
		return t
	}

	val places: List<Place>
		get() {
			return storedTour!!.places
		}

	fun retrievePlace(position: Int) {
		if (place != null && place!!.order == position) return
		when {
			position > storedTour!!.places.size -> {
				place = null
			}
			position < storedTour!!.places.size -> {
				place = storedTour!!.places[position]
			}
			else -> {
				place = Place()
				place!!.order = (position)
			}
		}
	}

	fun savePlace() {
		if (place!!.order == null || place!!.order!! >= storedTour!!.places.size) {
			place!!.order = (storedTour!!.places.size)
			storedTour!!.places.add(place!!)
		} else {
			storedTour!!.places[place!!.order!!] = place!!
		}
		place = null
	}

	fun reset() {
		storedTour = null
		place = null
		error = null
	}

	init {
		tourRepo = TourRepository.getInstance(application)
	}
}