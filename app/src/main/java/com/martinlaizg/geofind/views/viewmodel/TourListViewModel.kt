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

	fun getTours(stringQuery: String?): MutableLiveData<MutableList<Tour?>?> {
		val tours = MutableLiveData<MutableList<Tour?>?>()
		Thread(Runnable {
			try {
				if (stringQuery != null) {
					tours.postValue(tourRepo!!.getTours(stringQuery))
				} else {
					tours.postValue(tourRepo.getAllTours())
				}
			} catch (e: APIException) {
				error = e.type
				tours.postValue(null)
			}
		}).start()
		return tours
	}

	companion object {
		private val TAG = TourListViewModel::class.java.simpleName
	}

	init {
		tourRepo = TourRepository.Companion.getInstance(application)
	}
}