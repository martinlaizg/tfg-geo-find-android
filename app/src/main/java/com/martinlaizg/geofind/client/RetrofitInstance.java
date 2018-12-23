package com.martinlaizg.geofind.client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {
    private static final String BASE_URL = "https://geo-find-martinlaizg.herokuapp.com/api/";
    private static Retrofit retrofit;

    /**
     * Create an instance of Retrofit object
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static RestClient getRestClient() {
        Retrofit rf = getRetrofitInstance();
        return rf.create(RestClient.class);
    }
}
