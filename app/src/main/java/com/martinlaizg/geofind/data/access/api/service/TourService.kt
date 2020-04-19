package com.martinlaizg.geofind.data.access.api.service

import android.app.Application
import android.util.Log
import com.martinlaizg.geofind.data.access.api.RestClient
import com.martinlaizg.geofind.data.access.api.RetrofitFactory
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.api.error.ErrorUtils
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.access.database.entities.Tour
import retrofit2.Response
import java.util.*

class TourService private constructor(application: Application) {

	private val tag = TourService::class.simpleName
	private var restClient: RestClient = RetrofitFactory.getRestClient(application)

	companion object {
		private var instance: TourService? = null
		fun getInstance(application: Application): TourService {
			return instance ?: synchronized(this) {
				val newInstance = TourService(application)
				instance = newInstance
				newInstance
			}
		}
	}

	/**
	 * Retrieve all tours form de server
	 *
	 * @return the list of tours
	 * @throws APIException
	 * server errors
	 */
	@get:Throws(APIException::class)
	val allTours: MutableList<Tour>
		get() {
			val response: Response<MutableList<Tour>>
			var apiException: APIException?
			try {
				response = restClient.getTours(HashMap()).execute()
				if (response.isSuccessful) {
					return response.body() ?: mutableListOf()
				}
				apiException = ErrorUtils.parseError(response)
			} catch (e: Exception) {
				apiException = APIException(ErrorType.NETWORK, e.message!!)
				Log.e(tag, "getTours: ", e)
			}
			throw apiException!!
		}

	/**
	 * Create de tour in the server
	 *
	 * @param tour
	 * the tour to create
	 * @return the created tour
	 * @throws APIException
	 * server errors
	 */
	@Throws(APIException::class)
	fun create(tour: Tour): Tour? {
		val response: Response<Tour>
		var apiException: APIException
		try {
			response = restClient.createTour(tour).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(tag, "create: ", e)
		}
		throw apiException
	}

	/**
	 * Get a single tour by id
	 *
	 * @param id
	 * the id of the Tour to get
	 * @return the tour
	 * @throws APIException
	 * server errors
	 */
	@Throws(APIException::class)
	fun getTour(id: Int): Tour? {
		val response: Response<Tour?>
		var apiException: APIException?
		try {
			response = restClient.getTour(id).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(tag, "getTour: ", e)
		}
		throw apiException!!
	}

	/**
	 * Update the tour with id=tour.getId() with the data in tour
	 *
	 * @param tour
	 * the new data
	 * @return the new tour
	 * @throws APIException
	 * server errors
	 */
	@Throws(APIException::class)
	fun update(tour: Tour): Tour? {
		val response: Response<Tour?>
		var apiException: APIException?
		try {
			response = restClient.update(tour.id, tour).execute()
			if (response.isSuccessful) {
				return response.body()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(tag, "update: ", e)
		}
		throw apiException!!
	}

	@Throws(APIException::class)
	fun getTours(stringQuery: String): MutableList<Tour> {
		val response: Response<MutableList<Tour>>
		var apiException: APIException
		val params = HashMap<String, String>()
		params["q"] = stringQuery
		try {
			response = restClient.getTours(params).execute()
			if (response.isSuccessful) {
				return response.body() ?: mutableListOf()
			}
			apiException = ErrorUtils.parseError(response)
		} catch (e: Exception) {
			apiException = APIException(ErrorType.NETWORK, e.message!!)
			Log.e(tag, "update: ", e)
		}
		throw apiException
	}
}