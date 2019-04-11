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
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.fragment.creator.CreatorFragment;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class TourFragment
		extends Fragment {

	public static final String TOUR_ID = "TOUR_ID";
	private static final String TAG = TourFragment.class.getSimpleName();

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
	private Integer map_id;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		ButterKnife.bind(this, view);
		Bundle b = getArguments();
		if (b != null) {
			map_id = b.getInt(TOUR_ID);
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
		viewModel.getMap(map_id).observe(requireActivity(), this::setMap);
		viewModel.getLocations(map_id).observe(requireActivity(), locationEntities -> {
			if (locationEntities != null && !locationEntities.isEmpty()) {
				location_list.setVisibility(View.VISIBLE);
				adapter.setLocationEntities(locationEntities);
				map_num_locations.setText(String.format(getString(R.string.num_locations), locationEntities.size()));
			}
		});
		sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
	}

	private void setMap(Tour tour) {
		if (tour != null) {
			map_name.setText(tour.getName());
			map_description.setText(tour.getDescription());
			map_creator.setText(String.format(getString(R.string.num_creator), tour.getCreator_id().toString())); // TODO cambiar por valor real
			User u = Preferences.getLoggedUser(sp);
			if (u != null && u.getId().equals(tour.getCreator_id())) {
				Bundle b = new Bundle();
				b.putInt(CreatorFragment.TOUR_ID, tour.getId());
				edit_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toEditTour, b));
				edit_button.setVisibility(View.VISIBLE);
			}
		}
	}
}
