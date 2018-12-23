package com.martinlaizg.geofind.client;

import com.martinlaizg.geofind.client.login.LoginRequest;
import com.martinlaizg.geofind.client.login.LoginResponse;
import com.martinlaizg.geofind.entity.Location;
import com.martinlaizg.geofind.entity.Map;
import com.martinlaizg.geofind.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface RestClient {

    @GET("users")
    Call<List<User>> getUsers();

    @GET("maps")
    Call<List<Map>> getMap();

    @GET("locations")
    Call<List<Location>> getLocations();


    // Login user
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest body);
}
