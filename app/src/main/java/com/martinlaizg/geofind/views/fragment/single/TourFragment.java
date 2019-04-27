package com.martinlaizg.geofind.views.fragment.single;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.LocationListAdapter;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.fragment.creator.CreatorFragment;
import com.martinlaizg.geofind.views.fragment.play.PlayTourFragment;
import com.martinlaizg.geofind.views.viewmodel.TourViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

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

	@BindView(R.id.play_button)
	MaterialButton play_button;

	@BindView(R.id.empty_text)
	TextView empty_text;


	private SharedPreferences sp;
	private LocationListAdapter adapter;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tour, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());

		adapter = new LocationListAdapter();
		places_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		places_list.setAdapter(adapter);

		int tour_id = 0;
		Bundle b = getArguments();
		if (b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		if (tour_id == 0) {
			Toast.makeText(requireContext(), requireContext().getString(R.string.tour_not_permitted), Toast.LENGTH_SHORT).show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
		}
		TourViewModel viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel.class);
		viewModel.loadTour(tour_id).observe(requireActivity(), this::setTour);

	}

	private void setTour(Tour tour) {
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

			// Set places
			List<Place> places = tour.getPlaces();
			if (places != null && !places.isEmpty()) {
				adapter.setLocationEntities(places);
				tour_num_locations.setText(String.format(getString(R.string.num_places), places.size()));
				Bundle b = new Bundle();
				b.putInt(PlayTourFragment.TOUR_ID, tour.getId());
				play_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlayTour, b));
			} else {
				places_list.setVisibility(View.GONE);
				play_button.setVisibility(View.GONE);
			}
		}
	}
}
