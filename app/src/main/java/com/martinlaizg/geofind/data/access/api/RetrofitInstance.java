package com.martinlaizg.geofind.data.access.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.utils.DateUtils;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

	//	private static final String BASE_URL = "https://geofind1.herokuapp.com/api/";
	private static final String BASE_URL = "http://192.168.1.34:8000/api/";
	private static Retrofit retrofitInstance;

	public static RestClient getRestClient() {
		Retrofit rf = getRetrofitInstance();
		return rf.create(RestClient.class);
	}

	public static Retrofit getRetrofitInstance() {
		if(retrofitInstance == null) {
			Gson gson = new GsonBuilder().setDateFormat(DateUtils.DATE_FORMAT).create();
			retrofitInstance = new Retrofit.Builder().baseUrl(BASE_URL)
					.addConverterFactory(GsonConverterFactory.create(gson)).build();
		}

		return retrofitInstance;
	}
}
