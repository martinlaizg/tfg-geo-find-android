package com.martinlaizg.geofind.data.access.api.service

import android.app.Application
import android.util.Log
import com.martinlaizg.geofind.data.access.api.RestClient
import com.martinlaizg.geofind.data.access.api.RetrofitInstance
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Play
import retrofit2.Response
import java.io.IOException

class PlayService private constructor(application: Application) {
	private val restClient: RestClient?

	/**
	 * Get the user play
	 *
	 * @param userId
	 * the user id
	 * @param tourId
	 * the tour id
	 * @return the play
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun getUserPlay(userId: Int, tourId: Int): Play? {
		val response: Response<Play?>
		var apiException: APIException?
		try {
			response = restClient!!.getUserPlay(tourId, userId).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: IOException) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
		}
		throw apiException!!
	}

	/**
	 * Create the user play
	 *
	 * @param userId
	 * the user id
	 * @param tourId
	 * the tour id
	 * @return the play
	 * @throws APIException
	 * exception from server
	 */
	@Throws(APIException::class)
	fun createUserPlay(userId: Int, tourId: Int): Play? {
		val response: Response<Play?>
		var apiException: APIException?
		try {
			response = restClient!!.createUserPlay(tourId, userId).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: IOException) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
		}
		throw apiException!!
	}

	/**
	 * Create the place play
	 *
	 * @param playId
	 * the play id
	 * @param placeId
	 * the place id
	 * @return the play
	 * @throws APIException
	 * exception from the server
	 */
	@Throws(APIException::class)
	fun createPlacePlay(playId: Int?, placeId: Int?): Play? {
		val response: Response<Play?>
		var apiException: APIException?
		try {
			response = restClient!!.createPlacePlay(playId, placeId).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: IOException) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(TAG, "getPlace: ", e)
		}
		throw apiException!!
	}

	@Throws(APIException::class)
	fun getUserPlays(userId: Int): List<Play?>? {
		val response: Response<List<Play?>?>
		var apiException: APIException?
		try {
			response = restClient!!.getUserPlays(userId).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: IOException) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(TAG, "getPlace: ", e)
		}
		throw apiException!!
	}

	companion object {
		private val TAG = PlayService::class.java.simpleName
		private var instance: PlayService? = null
		fun getInstance(application: Application): PlayService? {
			if (instance == null) instance = PlayService(application)
			return instance
		}
	}

	init {
		restClient = RetrofitInstance.getRestClient(application)
	}
}