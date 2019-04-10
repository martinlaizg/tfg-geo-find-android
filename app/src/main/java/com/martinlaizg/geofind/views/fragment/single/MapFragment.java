package com.martinlaizg.geofind.views.fragment.single;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationListAdapter;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.PlaceEntity;
import com.martinlaizg.geofind.data.access.database.entities.TourEntity;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;
import com.martinlaizg.geofind.views.fragment.creator.CreateMapFragment;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class MapFragment
		extends Fragment {

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
	@BindView(R.id.edit_button)
	MaterialButton edit_button;
	@BindView(R.id.location_list)
	RecyclerView location_list;

	private SharedPreferences sp;
	private LocationListAdapter adapter;
	private String map_id;

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

		location_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new LocationListAdapter();
		location_list.setAdapter(adapter);
		location_list.setVisibility(View.GONE);

		MapViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MapViewModel.class);
		viewModel.getMap(map_id).observe(requireActivity(), new Observer<TourEntity>() {
			@Override
			public void onChanged(TourEntity tourEntity) {
				setMap(tourEntity);
			}
		});
		viewModel.getLocations(map_id).observe(requireActivity(), new Observer<List<PlaceEntity>>() {
			@Override
			public void onChanged(List<PlaceEntity> locationEntities) {
				if (locationEntities != null && !locationEntities.isEmpty()) {
					location_list.setVisibility(View.VISIBLE);
					adapter.setLocationEntities(locationEntities);
					map_num_locations.setText(String.format(getString(R.string.num_locations), locationEntities.size()));
				}
			}
		});
		sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
	}

	private void setMap(TourEntity tourEntity) {
		if (tourEntity != null) {
			map_name.setText(tourEntity.getName());
			map_description.setText(tourEntity.getDescription());
			map_creator.setText(String.format(getString(R.string.num_creator), tourEntity.getCreator_id())); // TODO cambiar por valor real
			UserEntity u = Preferences.getLoggedUser(sp);
			if (u != null && u.getId().equals(tourEntity.getCreator_id())) {
				Bundle b = new Bundle();
				b.putString(CreateMapFragment.MAP_ID, tourEntity.getId());
				edit_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toEditMap, b));
				edit_button.setVisibility(View.VISIBLE);
			}
		}
	}
}
