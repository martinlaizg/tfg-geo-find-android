package com.martinlaizg.geofind.client;

import com.martinlaizg.geofind.client.login.LoginRequest;
import com.martinlaizg.geofind.client.user.UserResponse;
import com.martinlaizg.geofind.entity.Location;
import com.martinlaizg.geofind.entity.Maps;
import com.martinlaizg.geofind.entity.User;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;

public interface RestClient {

    @GET("users")
    Call<List<User>> getUsers();

    // Get maps with optional filter
    @GET("maps")
    Call<List<Maps>> getMap(
            @QueryMap Map<String, String> params);

    // Get locations TODO: replace with the correct params or body
    @GET("locations")
    Call<List<Location>> getLocations(
            @QueryMap Map<String, String> params);

    // Login user
    @POST("login")
    Call<UserResponse> login(@Body LoginRequest body);
}
