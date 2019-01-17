package com.martinlaizg.geofind.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.martinlaizg.geofind.ItemClickListener;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.MapsAdapter;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.client.RetrofitInstance;
import com.martinlaizg.geofind.client.error.APIError;
import com.martinlaizg.geofind.client.error.ErrorUtils;
import com.martinlaizg.geofind.entity.Maps;

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

public class MapsListFragment extends Fragment {

    private static final String TAG = MapsListFragment.class.getSimpleName();
    SharedPreferences sp;

    private MapsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView recyclerView;

    private ArrayList<Maps> maps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        loadMaps();
        View view = inflater.inflate(R.layout.fragment_maps_list, container, false);
        recyclerView = view.findViewById(R.id.map_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        maps = new ArrayList<>();
        adapter = new MapsAdapter(maps);
        adapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onClick(View view, int position, boolean isLongClick) {
                Toast.makeText(getActivity(), "Has pulsado " + position, Toast.LENGTH_SHORT).show();
            }
        });
        recyclerView.setAdapter(adapter);

        return view;
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
                    Toast.makeText(getActivity(), "Mapas cargados correctamente", Toast.LENGTH_SHORT).show();
                    adapter.notifyItemInserted(1);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(getActivity(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Maps>> call, Throwable t) {
                Toast.makeText(getActivity(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
