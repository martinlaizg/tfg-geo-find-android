package com.martinlaizg.geofind.data.access.api.service

import android.app.Application
import android.util.Log
import com.martinlaizg.geofind.data.access.api.RestClient
import com.martinlaizg.geofind.data.access.api.RetrofitFactory
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.User
import retrofit2.Response
import java.util.*

class UserService private constructor(application: Application) {

	private val TAG = UserService::class.java.simpleName
	private var restClient: RestClient = RetrofitFactory.getRestClient(application)

	companion object {
		private var instance: UserService? = null
		fun getInstance(application: Application): UserService {
			return instance ?: synchronized(this) {
				val newInstance = UserService(application)
				instance = newInstance
				newInstance
			}
		}
	}

	/**
	 * Login request
	 * If login provider is own request /login/{provider}
	 * ie: /login/own
	 * ie: /login/google
	 *
	 * @param login
	 * login object
	 * @return the logged user
	 * @throws APIException
	 * api exception
	 */
	@Throws(APIException::class)
	fun login(login: Login): User? {
		val response: Response<User?>
		var apiException: APIException?
		try {
			response = restClient.login(login).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(TAG, "login: ", e)
		}
		throw apiException!!
	}

	/**
	 * Registry the user
	 *
	 * @param login
	 * the login data
	 * @return the registered user
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun registry(login: Login): User? {
		val response: Response<User?>
		var apiException: APIException?
		try {
			response = restClient.registry(login).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
		}
		throw apiException!!
	}

	/**
	 * Send message to support
	 *
	 * @param title
	 * the title
	 * @param message
	 * the message
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun sendMessage(title: String, message: String) {
		val response: Response<Void?>
		val apiException: APIException
		try {
			val params: MutableMap<String, String> = HashMap()
			params["title"] = title
			params["message"] = message
			response = restClient.sendSupportMessage(params).execute()
			if (response.isSuccessful) {
				return
			}
			throw ErrorUtils.parseError(response)!!
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			throw apiException
		}
	}

	/**
	 * Update user data
	 *
	 * @param login
	 * the login data
	 * @param user
	 * the user data
	 * @return the new user
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun update(login: Login, user: User): User? {
		val response: Response<User?>
		val apiException: APIException
		try {
			login.user = user
			response = restClient.updateUser(user.id, login).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			throw ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			throw apiException
		}
	}

}