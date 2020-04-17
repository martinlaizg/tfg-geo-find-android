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
import java.util.*

class CreatorViewModel(application: Application) : AndroidViewModel(application) {
	private val tourRepo: TourRepository?
	var storedTour: Tour? = null
		private set
	var error: ErrorType? = null
		private set
	var place: Place? = null
	fun createTour(): MutableLiveData<Tour?> {
		val m = MutableLiveData<Tour?>()
		Thread(label@ Runnable {
			if (storedTour.getId() == 0) { // Create tour
				try {
					storedTour = tourRepo!!.create(storedTour)
				} catch (e: APIException) {
					error = e.type
					m.postValue(null)
					return@label
				}
			} else { // Update tour
				try {
					storedTour = tourRepo!!.update(storedTour)
				} catch (e: APIException) {
					error = e.type
					m.postValue(null)
					return@label
				}
			}
			m.postValue(storedTour)
		}).start()
		return m
	}

	fun updateTour(name: String, description: String, creatorId: Int?, pl: PlayLevel,
	               imageUrl: String?) {
		if (storedTour == null) storedTour = Tour()
		storedTour!!.name = name
		storedTour!!.description = description
		storedTour.setCreator_id(creatorId)
		storedTour!!.min_level = pl
		storedTour.setImage(imageUrl)
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
		if (storedTour == null || storedTour.getId() != tourId) {
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
			val places = storedTour.getPlaces()
			return places ?: ArrayList()
		}

	fun retrievePlace(position: Int) {
		if (place != null && place.getOrder() == position) return
		if (position > storedTour.getPlaces().size) {
			place = null
		} else if (position < storedTour.getPlaces().size) {
			place = storedTour.getPlaces()[position]
		} else {
			place = Place()
			place.setOrder(position)
		}
	}

	fun savePlace() {
		if (place.getOrder() == null || place.getOrder() >= storedTour.getPlaces().size) {
			place.setOrder(storedTour.getPlaces().size)
			storedTour.getPlaces().add(place)
		} else {
			storedTour.getPlaces()[place.getOrder()] = place
		}
		place = null
	}

	fun reset() {
		storedTour = null
		place = null
		error = null
	}

	init {
		tourRepo = TourRepository.Companion.getInstance(application)
	}
}