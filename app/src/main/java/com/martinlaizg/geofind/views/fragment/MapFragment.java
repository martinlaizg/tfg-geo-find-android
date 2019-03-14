package com.martinlaizg.geofind.views.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationListAdapter;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment extends Fragment {

    public static final String MAP_ID = "MAP_ID";
    private static final String TAG = MapFragment.class.getSimpleName();

    @BindView(R.id.map_name)
    TextView map_name;
    @BindView(R.id.map_description)
    TextView map_description;
    @BindView(R.id.map_creator)
    TextView map_creator;
    @BindView(R.id.map_num_locations)
    TextView map_num_locations;

    @BindView(R.id.location_list)
    RecyclerView recyclerView;


    private MapViewModel viewModel;
    private LocationListAdapter adapter;
    private String map_id;


    public MapFragment() {
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        Bundle b = getArguments();
        if (b != null) {
            map_id = b.getString(MAP_ID);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LocationListAdapter();
        recyclerView.setAdapter(adapter);

        viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
        viewModel.getMap(map_id).observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                setMap(map);
            }
        });
        viewModel.getLocations(map_id).observe(getActivity(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setLocations(locations);
                map_num_locations.setText(String.valueOf(locations.size()));
            }
        });
    }

    private void setMap(Map map) {
        if (map != null) {
            map_name.setText(map.getName());
            map_description.setText(map.getDescription());
            map_creator.setText("Creador " + map.getCreator_id() + " TODO"); // TODO cambiar por valor real
        }
    }
}
