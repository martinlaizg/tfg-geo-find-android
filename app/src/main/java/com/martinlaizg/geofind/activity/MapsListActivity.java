package com.martinlaizg.geofind.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.MapsAdapter;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.client.RetrofitInstance;
import com.martinlaizg.geofind.client.error.APIError;
import com.martinlaizg.geofind.client.error.ErrorUtils;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.entity.Maps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsListActivity extends AppCompatActivity {

    private static final String TAG = MapsListActivity.class.getSimpleName();
    SharedPreferences sp;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private ArrayList<Maps> maps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_list);
        sp = Preferences.getInstance(getApplicationContext());
        initView();
        loadMaps();
    }

    private void initView() {
        recyclerView = findViewById(R.id.map_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        maps = new ArrayList<>();
        adapter = new MapsAdapter(maps);
        recyclerView.setAdapter(adapter);
    }

    private void loadMaps() {
        RestClient client = RetrofitInstance.getRestClient();
        Map<String, String> paramsMap = new HashMap<>();

        client.getMap(paramsMap).enqueue(new Callback<List<Maps>>() {
            @Override
            public void onResponse(Call<List<Maps>> call, Response<List<Maps>> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    // Parse response to JSON
                    List<Maps> listMaps = response.body();
                    maps.clear();
                    maps.addAll(listMaps);
                    // String mapsString = new Gson().toJson(maps);
                    Toast.makeText(getApplicationContext(), "Mapas cargados correctamente", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemInserted(1);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Maps>> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
