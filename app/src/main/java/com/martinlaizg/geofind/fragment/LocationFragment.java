package com.martinlaizg.geofind.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationAdapter;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.client.RetrofitInstance;
import com.martinlaizg.geofind.client.error.APIError;
import com.martinlaizg.geofind.client.error.ErrorUtils;
import com.martinlaizg.geofind.entity.Location;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LocationFragment extends Fragment {

    private static final String TAG = LocationFragment.class.getSimpleName();
    private ArrayList<Location> locations;
    private LocationAdapter adapter;
    private RecyclerView recyclerView;

    public LocationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        locations = new ArrayList<>();
        loadLocations();
        recyclerView = view.findViewById(R.id.location_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LocationAdapter(locations);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void loadLocations() {
        RestClient client = RetrofitInstance.getRestClient();
        Map<String, String> params = new HashMap<>();

        client.getLocations(params).enqueue(new Callback<List<Location>>() {
            @Override
            public void onResponse(Call<List<Location>> call, Response<List<Location>> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    // Parse response to JSON
                    List<Location> listLocation = response.body();
                    locations.clear();
                    locations.addAll(listLocation);
                    // String locationsString = new Gson().toJson(locations);
                    Toast.makeText(getActivity(), "Localizaciones cargados correctamente", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemInserted(1);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Location>> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
