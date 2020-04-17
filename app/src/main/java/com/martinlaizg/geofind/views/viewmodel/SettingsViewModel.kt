package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.repository.UserRepository

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
	private val userRepo: UserRepository?
	var error: ErrorType? = null
		private set

	fun sendMessage(title: String, message: String): MutableLiveData<Boolean?> {
		val r = MutableLiveData<Boolean?>()
		Thread(Runnable {
			try {
				userRepo!!.sendMessage(title, message)
				r.postValue(true)
			} catch (e: APIException) {
				Log.e(TAG, "sendMessage: ", e)
				error = e.type
				r.postValue(null)
			}
		}).start()
		return r
	}

	companion object {
		private val TAG = SettingsViewModel::class.java.simpleName
	}

	init {
		userRepo = UserRepository.Companion.getInstance(application)
	}
}