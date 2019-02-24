package com.martinlaizg.geofind.data.access.retrofit;

import android.content.Context;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
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

    static void ErrorToast(Context context) {
        Toast.makeText(context, context.getString(R.string.network_error), Toast.LENGTH_SHORT).show();
    }

    //Maps requests

    // Get maps with optional filter
    @GET("maps")
    Call<List<Map>> getMaps(@QueryMap java.util.Map<String, String> params);

    // Get single map
    @GET("maps/{id}")
    Call<Map> getSingleMap(@Path("id") String id);


    // Locations requests

    // Get locations
    @GET("locations")
    Call<List<Location>> getLocations(@QueryMap java.util.Map<String, String> params);

    // Get single location
    @GET("locations/{id}")
    Call<Location> getSingleLocation(@Path("id") String id);


    // User requests

    // Login user
    @POST("login")
    Call<User> login(@Body User body);

    @POST("users")
    Call<List<User>> getUsers(@QueryMap java.util.Map<String, String> params);
}
