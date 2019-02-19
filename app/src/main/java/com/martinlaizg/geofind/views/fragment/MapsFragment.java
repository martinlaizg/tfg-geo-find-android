package com.martinlaizg.geofind.views.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.MapsAdapter;
import com.martinlaizg.geofind.dataAccess.database.entity.Maps;
import com.martinlaizg.geofind.dataAccess.retrofit.RestClient;
import com.martinlaizg.geofind.dataAccess.retrofit.RetrofitInstance;
import com.martinlaizg.geofind.dataAccess.retrofit.error.APIError;
import com.martinlaizg.geofind.dataAccess.retrofit.error.ErrorUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsFragment extends Fragment {

    private static final String TAG = MapsFragment.class.getSimpleName();

    private MapsAdapter adapter;
    private RecyclerView recyclerView;

    private ArrayList<Maps> maps;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        loadMaps();
        final View view = inflater.inflate(R.layout.fragment_maps_list, container, false);
        recyclerView = view.findViewById(R.id.map_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        maps = new ArrayList<>();
        adapter = new MapsAdapter(getActivity(), maps);
        recyclerView.setAdapter(adapter);

        return view;
    }


    private void loadMaps() {
        final RestClient client = RetrofitInstance.getRestClient();
        final Map<String, String> paramsMap = new HashMap<>();

        client.getMap(paramsMap).enqueue(new Callback<List<Maps>>() {
            @Override
            public void onResponse(final Call<List<Maps>> call, final Response<List<Maps>> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    // Parse response to JSON
                    final List<Maps> listMaps = response.body();
                    maps.clear();
                    maps.addAll(listMaps);
                    // String mapsString = new Gson().toJson(maps);
                    adapter.notifyItemInserted(1);
                } else {
                    final APIError error = ErrorUtils.parseError(response);
                    final String errorMessage = error.getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(final Call<List<Maps>> call, final Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.connection_failure),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
