package com.martinlaizg.geofind.views.fragment.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationListAdapter;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.views.viewmodel.LocationViewModel;

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

public class LocationListFragment
		extends Fragment {

	private static final String TAG = LocationListFragment.class.getSimpleName();

	@BindView(R.id.location_list)
	RecyclerView recyclerView;

	private LocationListAdapter adapter;


	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location_list, container, false);
		ButterKnife.bind(this, view);
		ArrayList<PlaceEntity> locationEntities = new ArrayList<>();

		recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new LocationListAdapter();
		recyclerView.setAdapter(adapter);


		LocationViewModel locationViewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

		locationViewModel.getAllLocations().observe(this, new Observer<List<PlaceEntity>>() {
			@Override
			public void onChanged(List<PlaceEntity> locationEntities) {
				adapter.setLocationEntities(locationEntities);
			}
		});


		return view;
	}

}
