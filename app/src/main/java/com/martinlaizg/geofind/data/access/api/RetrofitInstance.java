package com.martinlaizg.geofind.data.access.api;

import android.app.Application;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;
import com.martinlaizg.geofind.utils.DateUtils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

	private static final String BASE_URL = "https://geofind1.herokuapp.com/api/";

	private static final String TAG = RetrofitInstance.class.getSimpleName();

	private static Retrofit retrofitInstance;

	private static String token;
	private static SharedPreferences sp;
	private static UserRepository userRepo;

	public static RestClient getRestClient(Application application) {
		if(userRepo == null) {
			userRepo = RepositoryFactory.getUserRepository(application);
		}
		if(sp == null) {
			sp = PreferenceManager.getDefaultSharedPreferences(application.getApplicationContext());
		}
		Retrofit rf = getRetrofitInstance();
		return rf.create(RestClient.class);
	}

	public static Retrofit getRetrofitInstance() {
		if(retrofitInstance == null) {

			OkHttpClient okHttpClient = new OkHttpClient.Builder().addInterceptor(chain -> {
				Request originalRequest = chain.request();
				Request newRequest = originalRequest.newBuilder()
						.addHeader("Authorization", getBearerToken())
						.method(originalRequest.method(), originalRequest.body()).build();

				Response response = chain.proceed(newRequest);
				if(response.code() == 401) { // Code 401 (Unathorized)
					try {
						userRepo.reLogin();
					} catch(APIException e) {
						Log.e(TAG, "Fail relogin", e);
					}
					newRequest = originalRequest.newBuilder()
							.addHeader("Authorization", getBearerToken())
							.method(originalRequest.method(), originalRequest.body()).build();
					response = chain.proceed(newRequest);
				} else {
					String newToken = response.header("Authorization");
					if(newToken != null && !newToken.isEmpty()) {
						Preferences.setToken(sp, newToken);
					}
				}
				return response;
			}).build();

			Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
			retrofitInstance = new Retrofit.Builder().baseUrl(BASE_URL).client(okHttpClient)
					.addConverterFactory(GsonConverterFactory.create(gson)).build();
		}

		return retrofitInstance;
	}

	private static String getBearerToken() {
		return "Bearer " + token;
	}

	public static void setToken(String token) {
		RetrofitInstance.token = token;
	}
}
