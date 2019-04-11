package com.martinlaizg.geofind.views.fragment.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.CreatorPlacesAdapter;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.views.fragment.single.TourFragment;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatorFragment
		extends Fragment
		implements View.OnClickListener {

	public static final String TOUR_ID = "TOUR_ID";

	// View
	@BindView(R.id.tour_name)
	TextView tour_name;
	@BindView(R.id.create_map_description)
	TextView tour_description;

	@BindView(R.id.add_place_button)
	MaterialButton add_place_button;
	@BindView(R.id.create_map_button)
	MaterialButton create_tour_button;
	@BindView(R.id.edit_button)
	MaterialButton edit_button;

	@BindView(R.id.places_rv)
	RecyclerView places_rv;

	private CreatorViewModel viewModel;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_creator, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		CreatorPlacesAdapter adapter = new CreatorPlacesAdapter();
		places_rv.setLayoutManager(new LinearLayoutManager(requireActivity()));
		places_rv.setAdapter(adapter);

		// flag control
		if (!viewModel.isLoaded()) {
			try {
				viewModel.loadTour(getArguments()).observe(this, tour -> {
					adapter.setPlaces(tour.getPlaces());
					if (tour.getId() != 0) {
						create_tour_button.setText(R.string.update_tour);
					}
					if (!tour.getName().isEmpty()) {
						tour_name.setText(tour.getName());
					}
					if (!tour.getDescription().isEmpty()) {
						tour_description.setText(tour.getDescription());
					}
				});
			} catch (APIException e) {
				viewModel.clear();
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
				return;
			}
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toCreateTour);
			return;
		}
		// disable flag
		viewModel.setLoaded(false);

		// set buttons
		add_place_button.setOnClickListener(v -> {
			Bundle b = new Bundle();
			b.putInt(CreatePlaceFragment.PLACE_POSITION, viewModel.getPlaces().size());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toCreatePlace, b);
		});
		edit_button.setOnClickListener(v -> Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack());
		create_tour_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		create_tour_button.setEnabled(false);
		viewModel.createTour().observe(this, tour -> {
			create_tour_button.setEnabled(true);
			if (tour == null) {
				// TODO manage error
				APIException err = viewModel.getError();
				Toast.makeText(requireActivity(), err.getType().getMessage(), Toast.LENGTH_LONG).show();
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack(R.id.navTourList, false);
				viewModel.clear();
				return;
			}
			viewModel.clear();
			Toast.makeText(requireActivity(), R.string.tour_created, Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
			b.putInt(TourFragment.TOUR_ID, tour.getId());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toNewTour, b);
		});
	}
}
