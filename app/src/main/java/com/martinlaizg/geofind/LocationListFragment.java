package com.martinlaizg.geofind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.adapter.LocationAdapter;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.views.model.LocationViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationListFragment extends Fragment {

    private static final String TAG = LocationListFragment.class.getSimpleName();

    @BindView(R.id.location_list)
    RecyclerView recyclerView;

    private ArrayList<Location> locations;
    private LocationAdapter adapter;
    private LocationViewModel locationViewModel;


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        ButterKnife.bind(this, view);
        locations = new ArrayList<>();

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new LocationAdapter();
        recyclerView.setAdapter(adapter);


        locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        locationViewModel.getAllLocations().observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setLocations(locations);
            }
        });


        return view;
    }

}
