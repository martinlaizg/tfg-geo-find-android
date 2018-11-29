package com.martinlaizg.geofind.client;

import com.martinlaizg.geofind.entity.User;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestClient {

    @GET("users")
    Call<List<User>> getUsers();


}
