package com.martinlaizg.geofind.data.repository

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager.getDefaultSharedPreferences
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.service.UserService
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.AppDatabase
import com.martinlaizg.geofind.data.access.database.dao.UserDAO
import com.martinlaizg.geofind.data.access.database.entities.User

class UserRepository private constructor(application: Application) {

	private val tag = UserRepository::class.simpleName

	private val userDAO: UserDAO
	private val userService: UserService
	private val sp: SharedPreferences

	companion object {
		private var instance: UserRepository? = null
		fun getInstance(application: Application): UserRepository {
			return instance ?: synchronized(this) {
				val newInstance = UserRepository(application)
				instance = newInstance
				newInstance
			}
		}

		fun getInstance(): UserRepository? {
			return instance
		}
	}

	init {
		Log.d(tag, "Instanced")
		val database: AppDatabase = AppDatabase.getDatabase(application)
		userDAO = database.userDAO()
		userService = UserService.getInstance(application)
		sp = getDefaultSharedPreferences(application)
	}

	/**
	 * Registry a user
	 *
	 * @param login
	 * login data to be registered (email, username and password only)
	 * @return the user registered
	 * @throws APIException
	 * exception from API
	 */
	@Throws(APIException::class)
	fun registry(login: Login): User? {
		val user = userService.registry(login)
		if (user != null) {
			userDAO.insert(user)
			Preferences.setLogin(sp, login)
		}
		return user
	}

	/**
	 * Insert a user into the database
	 *
	 * @param user
	 * user to be inserted
	 */
	fun insert(user: User?) {
		if (user != null) {
			val u = userDAO.getUser(user.id)
			if (u == null) {
				userDAO.insert(user)
			} else {
				userDAO.update(user)
			}
		}
	}

	/**
	 * Get a user from database
	 *
	 * @param userId
	 * id of the user
	 * @return the user
	 */
	fun getUser(userId: Int): User? {
		return userDAO.getUser(userId)
	}

	/**
	 * Send message to support
	 *
	 * @param title
	 * the title
	 * @param message
	 * the message
	 * @throws APIException
	 * the exception
	 */
	@Throws(APIException::class)
	fun sendMessage(title: String, message: String) {
		userService.sendMessage(title, message)
	}

	/**
	 * Update the user on the server
	 *
	 * @param login
	 * the user login
	 * @param user
	 * the new user data
	 * @return the new User
	 * @throws APIException
	 * the API exception
	 */
	@Throws(APIException::class)
	fun updateUser(login: Login, user: User): User? {
		val u = userService.update(login, user)
		u?.let { userDAO.insert(it) }
		return u
	}

	@Throws(APIException::class)
	fun reLogin() {
		login(Preferences.getLogin(sp))
	}

	/**
	 * Login a user
	 *
	 * @param login
	 * login data to login (email and password only)
	 * @return the logged user
	 * @throws APIException
	 * exception from API
	 */
	@Throws(APIException::class)
	fun login(login: Login?): User? {
		val user = userService.login(login!!)
		if (user != null) {
			userDAO.insert(user)
			Preferences.setLogin(sp, login)
		}
		return user
	}
}