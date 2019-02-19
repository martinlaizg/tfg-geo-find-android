package com.martinlaizg.geofind.dataAccess.retrofit;

import com.martinlaizg.geofind.dataAccess.database.entity.Location;
import com.martinlaizg.geofind.dataAccess.database.entity.Maps;
import com.martinlaizg.geofind.dataAccess.database.entity.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RestClient {

    // Get maps with optional filter
    @GET("maps")
    Call<List<Maps>> getMap(@QueryMap Map<String, String> params);

    // Get locations
    @GET("locations")
    Call<List<Location>> getLocations(@QueryMap Map<String, String> params);

    // Get single map
    @GET("maps/{id}")
    Call<Maps> getSingleMap(@Path("id") String id);

    // Login user
    @POST("login")
    Call<User> login(@Body User body);

    // Create a Map
    @POST("maps")
    Call<Maps> createMap(@Body Maps map);
}
