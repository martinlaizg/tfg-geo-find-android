package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.repository.PlayRepository
import com.martinlaizg.geofind.data.repository.TourRepository

class TourViewModel(application: Application) : AndroidViewModel(application) {
	private val tourRepo: TourRepository?
	private val playRepo: PlayRepository?
	var error: ErrorType? = null
		private set
	private var tour: Tour? = null
	private var play: Play? = null
	fun getTour(tourId: Int, userId: Int): MutableLiveData<Tour?> {
		val m = MutableLiveData<Tour?>()
		Thread(Runnable {
			try {
				tour = tourRepo!!.getTour(tourId)
				play = playRepo!!.getPlay(userId, tourId)
			} catch (e: APIException) {
				error = e.type
				tour = null
			}
			m.postValue(tour)
		}).start()
		return m
	}

	fun getPlace(placeId: Int): MutableLiveData<Place?> {
		val p = MutableLiveData<Place?>()
		Thread(Runnable {
			for (place in tour?.places ?: ArrayList()) {
				if (place.id == placeId) {
					p.postValue(place)
					return@Runnable
				}
			}
		}).start()
		return p
	}

	val places: List<Place>
		get() = tour?.places ?: ArrayList()

	val playPlaces: List<Place>
		get() = play?.places ?: ArrayList()

	init {
		tourRepo = TourRepository.getInstance(application)
		playRepo = PlayRepository.getInstance(application)
	}
}