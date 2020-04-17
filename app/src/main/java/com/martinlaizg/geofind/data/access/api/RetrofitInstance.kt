package com.martinlaizg.geofind.data.access.api

import android.app.Application
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.google.gson.GsonBuilder
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException
import com.martinlaizg.geofind.data.repository.UserRepository
import com.martinlaizg.geofind.utils.DateUtils
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://geofind1.herokuapp.com/api/"
    private val TAG = RetrofitInstance::class.java.simpleName

    // Code 401 Unauthorized
    var retrofitInstance: Retrofit? = null
        get() {
            if (field == null) {
                val okHttpClient = OkHttpClient.Builder().addInterceptor { chain: Interceptor.Chain ->
                    val originalRequest = chain.request()
                    var newRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", bearerToken)
                            .method(originalRequest.method(), originalRequest.body()).build()
                    var response = chain.proceed(newRequest)
                    if (response.code() == 401) { // Code 401 Unauthorized
                        try {
                            if (userRepo == null) {
                                userRepo = UserRepository.Companion.getInstance()
                            }
                            userRepo!!.reLogin()
                        } catch (e: APIException) {
                            Log.e(TAG, "Fail relogin", e)
                        }
                        newRequest = originalRequest.newBuilder()
                                .addHeader("Authorization", bearerToken)
                                .method(originalRequest.method(), originalRequest.body()).build()
                        response = chain.proceed(newRequest)
                    } else {
                        val newToken = response.header("Authorization")
                        if (newToken != null && !newToken.isEmpty()) {
                            Preferences.setToken(sp, newToken)
                        }
                    }
                    response
                }.build()
                val gson = GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create()
                field = Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
                        .addConverterFactory(GsonConverterFactory.create(gson)).build()
            }
            return field
        }
        private set
    private var token: String? = null
    private var sp: SharedPreferences? = null
    private var userRepo: UserRepository? = null
    fun getRestClient(application: Application): RestClient {
        if (sp == null) {
            sp = PreferenceManager.getDefaultSharedPreferences(application.applicationContext)
        }
        val rf = retrofitInstance
        return rf!!.create(RestClient::class.java)
    }

    private val bearerToken: String
        private get() = "Bearer $token"

    fun setToken(token: String?) {
        RetrofitInstance.token = token
    }
}