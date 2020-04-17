package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.data.repository.PlayRepository

class MainFragmentViewModel(application: Application) : AndroidViewModel(application) {
	private val playRepo: PlayRepository?
	var error: ErrorType? = null
		private set

	fun getUserPlays(userId: Int): MutableLiveData<List<Play?>?> {
		val plays = MutableLiveData<List<Play?>?>()
		Thread(Runnable {
			try {
				plays.postValue(playRepo!!.getUserPlays(userId))
			} catch (e: APIException) {
				error = e.type
				plays.postValue(null)
			}
		}).start()
		return plays
	}

	companion object {
		private val TAG = MainFragmentViewModel::class.java.simpleName
	}

	init {
		playRepo = PlayRepository.Companion.getInstance(application)
	}
}