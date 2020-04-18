package com.martinlaizg.geofind.data.access.api

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.data.repository.UserRepository
import com.martinlaizg.geofind.utils.DateUtils
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitFactory {
	private val TAG = RetrofitFactory::class.simpleName
	private val BASE_URL: String = "https://geofind1.herokuapp.com/api/"

	var token: String? = null
	private val bearerToken: String
		get() = "Bearer $token"

	private var userRepo: UserRepository? = null

	private var sp: SharedPreferences? = null
	private var retrofitInstance: Retrofit? = null

	fun getRetrofitInstance(): Retrofit {
		return retrofitInstance ?: synchronized(this) {
			Log.d(TAG, "Instantiate")
			val okHttpClient = OkHttpClient.Builder()
					.addInterceptor(AuthInterceptor(userRepo, sp!!, bearerToken))
					.build()
			val gson = GsonBuilder()
					.setDateFormat(DateUtils.DATE_FORMAT)
					.create()
			val newInstance = Retrofit.Builder()
					.baseUrl(BASE_URL).client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create(gson))
					.build()
			retrofitInstance = newInstance
			newInstance
		}
	}

	fun getRestClient(application: Application): RestClient {
		if (sp == null) {
			sp = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
		}
		val rf = retrofitInstance
		return rf!!.create(RestClient::class.java)
	}
}