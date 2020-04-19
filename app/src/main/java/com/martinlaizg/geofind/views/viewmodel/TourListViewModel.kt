package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.repository.TourRepository

class TourListViewModel(application: Application) : AndroidViewModel(application) {
	private val tourRepo: TourRepository?
	var error: ErrorType? = null
		private set

	fun getTours(stringQuery: String): MutableLiveData<MutableList<Tour>> {
		val tours = MutableLiveData<MutableList<Tour>>()
		Thread(Runnable {
			try {
				tours.postValue(tourRepo!!.getTours(stringQuery))
			} catch (e: APIException) {
				error = e.type
				tours.postValue(null)
			}
		}).start()
		return tours
	}

	init {
		tourRepo = TourRepository.getInstance(application)
	}
}