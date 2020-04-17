package com.martinlaizg.geofind.views.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.data.repository.UserRepository

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {
	private val userRepo: UserRepository?
	var error: ErrorType? = null
		private set

	fun updateUser(login: Login?, user: User?): MutableLiveData<User?> {
		val u = MutableLiveData<User?>()
		Thread(Runnable {
			try {
				u.postValue(userRepo!!.updateUser(login, user))
			} catch (e: APIException) {
				error = e.type
				u.postValue(null)
			}
		}).start()
		return u
	}

	init {
		userRepo = UserRepository.Companion.getInstance(application)
	}
}