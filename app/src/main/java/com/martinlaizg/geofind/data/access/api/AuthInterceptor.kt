package com.martinlaizg.geofind.data.access.api

import android.content.SharedPreferences
import android.util.Log
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.repository.UserRepository
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private var userRepo: UserRepository?,
                      private val sp: SharedPreferences,
                      private val bearerToken: String) : Interceptor {

	private val tag: String? = AuthInterceptor::class.simpleName

	override fun intercept(chain: Interceptor.Chain): Response {
		val originalRequest = chain.request()
		var newRequest = originalRequest.newBuilder()
				.addHeader("Authorization", bearerToken)
				.method(originalRequest.method(), originalRequest.body()).build()
		var response = chain.proceed(newRequest)
		if (response.code() == 401) { // Code 401 Unauthorized
			try {
				if (userRepo == null) {
					userRepo = UserRepository.getInstance()
				}
				userRepo!!.reLogin()
			} catch (e: APIException) {
				Log.e(tag, "Fail login", e)
			}
			newRequest = originalRequest.newBuilder()
					.addHeader("Authorization", bearerToken)
					.method(originalRequest.method(), originalRequest.body()).build()
			response = chain.proceed(newRequest)
		} else {
			val newToken: String = response.header("Authorization") ?: ""
			if (newToken.isNotEmpty()) {
				Preferences.setToken(sp, newToken)
			}
		}
		return response
	}
}
