package com.martinlaizg.geofind.views.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationListAdapter;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment
		extends Fragment {

	public static final String MAP_ID = "MAP_ID";
	public static final String PLAY_LEVEL = "PLAY_LEVEL";
	private static final String TAG = MapFragment.class.getSimpleName();

	@BindView(R.id.map_name)
	TextView map_name;
	@BindView(R.id.map_description)
	TextView map_description;
	@BindView(R.id.map_creator)
	TextView map_creator;
	@BindView(R.id.map_num_locations)
	TextView map_num_locations;

	@BindView(R.id.play_therm_button)
	MaterialButton play_therm_button;
	@BindView(R.id.play_compass_button)
	MaterialButton play_compass_button;
	@BindView(R.id.play_map_button)
	MaterialButton play_map_button;


	@BindView(R.id.location_list)
	RecyclerView recyclerView;
	@BindView(R.id.play_buttons)
	ConstraintLayout play_buttons;

	private MapViewModel viewModel;
	private LocationListAdapter adapter;
	private String map_id;
	private PlayLevel play_level;


	public MapFragment() {
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		ButterKnife.bind(this, view);
		Bundle b = getArguments();
		if (b != null) {
			map_id = b.getString(MAP_ID);
			int p = b.getInt(PLAY_LEVEL, -1);
			if (p >= 0) { // play level selected
				play_level = PlayLevel.values()[p];
				play_buttons.setVisibility(View.GONE);
			} else { // play level not selected
				play_buttons.setVisibility(View.VISIBLE);
			}
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
		adapter = new LocationListAdapter();
		adapter.setPlayLevel(play_level);
		recyclerView.setAdapter(adapter);

		play_map_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlayMapOnMap));
		// TODO change to the correct navigation
		play_compass_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlayMapOnMap));
		// TODO change to the correct navigation
		play_therm_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlayMapOnMap));

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
			setPlayLevel(map.getMin_level());
		}
	}

	private void setPlayLevel(PlayLevel min_level) {
		play_map_button.setEnabled(false);
		play_compass_button.setEnabled(false);
		play_therm_button.setEnabled(false);
		switch (min_level) {
			case MAP:
				play_map_button.setEnabled(true);
			case COMPASS:
				play_compass_button.setEnabled(true);
			case THERMOMETER:
				play_therm_button.setEnabled(true);
		}
	}
}
