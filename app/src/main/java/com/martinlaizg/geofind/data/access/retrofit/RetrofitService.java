package com.martinlaizg.geofind.data.access.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitService {
	//	private static final String BASE_URL = "https://geo-find-martinlaizg.herokuapp.com/api/";
	private static final String BASE_URL = "http://192.168.1.33:8000/api/";
	private static retrofit2.Retrofit retrofit;

	/**
	 * Create an instance of RetrofitService object
	 */
	public static retrofit2.Retrofit getRetrofitInstance() {
		if (retrofit == null) {
			Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
			retrofit = new retrofit2.Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create(gson)).build();
		}

		return retrofit;
	}

	public static RestClient getRestClient() {
		retrofit2.Retrofit rf = getRetrofitInstance();
		return rf.create(RestClient.class);
	}
}
