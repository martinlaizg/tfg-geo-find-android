package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.data.repository.PlayRepository
import java.util.*

class PlayTourViewModel(application: Application) : AndroidViewModel(application) {

	private val playRepo: PlayRepository = PlayRepository.getInstance(application)

	var error: ErrorType? = null
		get() = if (field != null) field else ErrorType.OTHER
		private set
	var play: Play? = null
		private set

	fun loadPlay(userId: Int, tourId: Int): MutableLiveData<Place?> {
		val m = MutableLiveData<Place?>()
		Thread(Runnable {
			try {
				play = playRepo.getPlay(userId, tourId)
				if (play == null) {
					play = playRepo.createPlay(userId, tourId)
				}
				m.postValue(nextPlace)
			} catch (e: APIException) {
				error = e.type
			}
		}).start()
		return m
	}

	private val nextPlace: Place?
		get() {
			val totalPlaces: List<Place> = ArrayList(play!!.tour!!.places)
			val idsPlayedPlaces = play!!.places.map(Place::id)
			val notPlayedPlaces = totalPlaces.filter { !idsPlayedPlaces.contains(it.id) }
			return if (totalPlaces.isEmpty()) null else notPlayedPlaces.first()
		}

	fun completePlace(placeId: Int): MutableLiveData<Place?> {
		val c = MutableLiveData<Place?>()
		Thread(Runnable {
			try {
				if (play!!.id == 0) {
					play = playRepo.createPlay(play!!.userId, play!!.userId)
				}
				play = playRepo.completePlace(play!!.id, placeId)
				c.postValue(nextPlace)
			} catch (e: APIException) {
				error = e.type
				c.postValue(null)
			}
		}).start()
		return c
	}

	fun tourIsCompleted(): Boolean {
		return play!!.places.size == play!!.tour!!.places.size
	}
}