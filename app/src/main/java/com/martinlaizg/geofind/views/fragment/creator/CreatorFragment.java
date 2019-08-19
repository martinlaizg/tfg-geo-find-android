package com.martinlaizg.geofind.views.fragment.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.views.adapter.CreatorPlacesAdapter;
import com.martinlaizg.geofind.views.fragment.single.TourFragment;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatorFragment
		extends Fragment
		implements View.OnClickListener {

	public static final String TOUR_ID = "TOUR_ID";
	private static final String TAG = CreatorFragment.class.getSimpleName();

	@BindView(R.id.tour_name)
	TextView tour_name;
	@BindView(R.id.tour_description)
	TextView tour_description;

	@BindView(R.id.add_place_button)
	MaterialButton add_place_button;
	@BindView(R.id.create_tour_button)
	MaterialButton create_tour_button;
	@BindView(R.id.edit_button)
	MaterialButton edit_button;

	@BindView(R.id.places_list)
	RecyclerView places_list;

	private CreatorViewModel viewModel;
	private CreatorPlacesAdapter adapter;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_creator, container, false);
		ButterKnife.bind(this, view);
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		adapter = new CreatorPlacesAdapter();
		places_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		places_list.setAdapter(adapter);

		ItemTouchHelper helper = new ItemTouchHelper(
				new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0) {
					@Override
					public boolean onMove(@NonNull RecyclerView recyclerView,
							@NonNull RecyclerView.ViewHolder dragged,
							@NonNull RecyclerView.ViewHolder target) {
						int from = dragged.getAdapterPosition();
						int to = target.getAdapterPosition();

						adapter.move(from, to);
						return false;
					}

					@Override
					public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder,
							int direction) {

					}
				});
		helper.attachToRecyclerView(places_list);

		// Back button callback
		OnBackPressedCallback callback = new OnBackPressedCallback(true) {
			@Override
			public void handleOnBackPressed() {
				showExitDialog();
			}
		};
		requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);
		return view;
	}

	private void showExitDialog() {
		new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
				.setMessage(getString(R.string.exit_lose_data_alert))
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> Navigation
						.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack()).show();
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		Bundle b = getArguments();
		int tour_id = 0;
		if(b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		viewModel.getTour(tour_id).observe(this, this::setTour);

		add_place_button.setOnClickListener(v -> {
			Bundle c = new Bundle();
			c.putInt(CreatePlaceFragment.PLACE_POSITION, viewModel.getPlaces().size());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toCreatePlace, c);
		});
		int finalTour_id = tour_id;
		edit_button.setOnClickListener(v -> {
			Bundle c = new Bundle();
			c.putInt(CreateTourFragment.TOUR_ID, finalTour_id);

			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toEditTour, c);
		});
		create_tour_button.setOnClickListener(this);
	}

	private void setTour(Tour tour) {
		if(tour == null) {
			ErrorType error = viewModel.getError();
			return;
		}
		adapter.setPlaces(requireActivity(), tour.getPlaces());
		if(tour.getId() != 0) {
			create_tour_button.setText(R.string.update_tour);
		}
		if(tour.getName().isEmpty()) {
			tour_name.setText(getString(R.string.click_edit));
		} else {
			tour_name.setText(tour.getName());
		}
		if(tour.getDescription().isEmpty()) {
			tour_description.setText(getResources().getString(R.string.without_decription));
		} else {
			tour_description.setText(tour.getDescription());
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		viewModel.reset();
	}

	@Override
	public void onClick(View v) {
		if(viewModel.getStoredTour().getPlaces().size() == 0) {
			Toast.makeText(requireContext(), getString(R.string.at_least_one_place),
			               Toast.LENGTH_SHORT).show();

			return;
		}
		create_tour_button.setEnabled(false);
		viewModel.createTour().observe(this, tour -> {
			create_tour_button.setEnabled(true);
			if(tour == null) {
				ErrorType err = viewModel.getError();
				Toast.makeText(requireActivity(), err.toString(), Toast.LENGTH_LONG).show();
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack(R.id.navTourList, false);
				return;
			}
			Toast.makeText(requireActivity(), R.string.tour_created, Toast.LENGTH_SHORT).show();
			Bundle b = new Bundle();
			b.putInt(TourFragment.TOUR_ID, tour.getId());
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toNewTour, b);
		});
	}

}
