package com.martinlaizg.geofind.data.access.retrofit;

import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface RestClient {

    // Get maps with optional filter
    @GET("maps")
    Call<List<Map>> getMap(@QueryMap java.util.Map params);

    // Get locations
    @GET("locations")
    Call<List<Location>> getLocations(@QueryMap java.util.Map params);

    // Get single map
    @GET("maps/{id}")
    Call<Map> getSingleMap(@Path("id") String id);

    // Login user
    @POST("login")
    Call<User> login(@Body User body);

    // Create a Map
    @POST("maps")
    Call<Map> createMap(@Body Map map);
}
