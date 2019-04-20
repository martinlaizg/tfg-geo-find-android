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

	@BindView(R.id.tour_name)
	TextView tour_name;
	@BindView(R.id.tour_description)
	TextView tour_description;
	@BindView(R.id.tour_creator)
	TextView tour_creator;
	@BindView(R.id.tour_num_locations)
	TextView tour_num_locations;
	@BindView(R.id.edit_button)
	MaterialButton edit_button;
	@BindView(R.id.places_list)
	RecyclerView places_list;

	@BindView(R.id.empty_text)
	TextView empty_text;

	private SharedPreferences sp;
	private LocationListAdapter adapter;
	private Integer tour_id;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_map, container, false);
		ButterKnife.bind(this, view);
		Bundle b = getArguments();
		if (b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		places_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		adapter = new LocationListAdapter();
		places_list.setAdapter(adapter);
		places_list.setVisibility(View.GONE);

		MapViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MapViewModel.class);
		viewModel.getMap(tour_id).observe(requireActivity(), this::setMap);
		viewModel.getLocations(tour_id).observe(requireActivity(), locationEntities -> {
			if (locationEntities != null && !locationEntities.isEmpty()) {
				places_list.setVisibility(View.VISIBLE);
				adapter.setLocationEntities(locationEntities);
				tour_num_locations.setText(String.format(getString(R.string.num_locations), locationEntities.size()));
			}
		});
		sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
	}

	private void setMap(Tour tour) {
		if (tour != null) {
			tour_name.setText(tour.getName());
			tour_description.setText(tour.getDescription());
			tour_creator.setText(tour.getCreator().getUsername());
			if (tour.getPlaces().isEmpty()) {
				empty_text.setVisibility(View.VISIBLE);
			}
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
