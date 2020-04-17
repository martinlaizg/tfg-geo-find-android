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
	private val playRepo: PlayRepository?
	var error: ErrorType? = null
		get() = if (field != null) field else ErrorType.OTHER
		private set
	var play: Play? = null
		private set

	fun loadPlay(userId: Int, tourId: Int): MutableLiveData<Place?> {
		val m = MutableLiveData<Place?>()
		Thread(Runnable {
			try {
				play = playRepo!!.getPlay(userId, tourId)
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
		private get() {
			val places: List<Place?> = ArrayList(play.getTour().places)
			var i = 0
			while (i < places.size) {
				for (p in play.getPlaces()) {
					if (places[i].getId() == p.id) {
						places.removeAt(i)
						i--
						break
					}
				}
				i++
			}
			return if (places.size == 0) null else places[0]
		}

	fun completePlace(placeId: Int): MutableLiveData<Place?> {
		val c = MutableLiveData<Place?>()
		Thread(Runnable {
			try {
				if (play.getId() == 0) {
					play = playRepo!!.createPlay(play.getUser_id(), play.getUser_id())
				}
				play = playRepo!!.completePlace(play.getId(), placeId)
				c.postValue(nextPlace)
			} catch (e: APIException) {
				error = e.type
				c.postValue(null)
			}
		}).start()
		return c
	}

	fun tourIsCompleted(): Boolean {
		return play.getPlaces().size == play.getTour().places.size
	}

	init {
		playRepo = PlayRepository.Companion.getInstance(application)
	}
}