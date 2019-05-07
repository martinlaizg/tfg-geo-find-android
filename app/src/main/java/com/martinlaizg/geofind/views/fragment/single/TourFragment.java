package com.martinlaizg.geofind.views.fragment.single;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
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
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.adapter.PlaceListAdapter;
import com.martinlaizg.geofind.views.fragment.creator.CreatorFragment;
import com.martinlaizg.geofind.views.fragment.play.PlayMapFragment;
import com.martinlaizg.geofind.views.viewmodel.TourViewModel;

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
	@BindView(R.id.tour_num_places)
	TextView tour_num_places;
	@BindView(R.id.edit_button)
	MaterialButton edit_button;
	@BindView(R.id.places_list)
	RecyclerView places_list;
	@BindView(R.id.other_places)
	RecyclerView other_places;

	@BindView(R.id.play_button)
	MaterialButton play_button;

	@BindView(R.id.empty_text)
	TextView empty_text;

	private SharedPreferences sp;
	private PlaceListAdapter adapterCompleted;
	private PlaceListAdapter adapterNoCompleted;
	private AlertDialog alert;
	private TourViewModel viewModel;
	private User user;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_tour, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
		user = Preferences.getLoggedUser(sp);

		adapterCompleted = new PlaceListAdapter(true, getResources().getColor(R.color.grey));
		places_list.setLayoutManager(new LinearLayoutManager(requireActivity()));
		places_list.setAdapter(adapterCompleted);

		adapterNoCompleted = new PlaceListAdapter(false, 0);
		other_places.setLayoutManager(new LinearLayoutManager(requireActivity()));
		other_places.setAdapter(adapterNoCompleted);

		int tour_id = 0;
		Bundle b = getArguments();
		if(b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		if(tour_id == 0) {
			Toast.makeText(requireContext(),
			               requireContext().getString(R.string.tour_not_permitted),
			               Toast.LENGTH_SHORT).show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
		}
		viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel.class);
		viewModel.loadTour(tour_id, user.getId()).observe(requireActivity(), this::setTour);

		Bundle c = new Bundle();
		c.putInt(PlayMapFragment.TOUR_ID, tour_id);
		String[] difficulties = getResources().getStringArray(R.array.difficulties);
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		builder.setTitle(getResources().getString(R.string.select_play_difficulty));
		builder.setItems(difficulties, (dialog, item) -> {
			int destination = R.id.toPlayTour;
			switch(item) {
				case 1:
					destination = R.id.toPlayCompass;
					Log.i(TAG, "onViewCreated: go to play with compass");
					break;
				case 2:
					destination = R.id.toPlayTherm;
					Log.i(TAG, "onViewCreated: go to play with therm");
					break;
				default:
					Log.i(TAG, "onViewCreated: go to play on map");
			}
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(destination, c);
		});
		alert = builder.create();
	}

	private void setTour(Tour tour) {
		if(tour != null) {
			tour_name.setText(tour.getName());
			tour_description.setText(tour.getDescription());
			tour_creator.setText(tour.getCreator().getUsername());
			if(tour.getPlaces().isEmpty()) {
				empty_text.setVisibility(View.VISIBLE);
			}
			if(user != null && user.getId() == tour.getCreator_id()) {
				Bundle b = new Bundle();
				b.putInt(CreatorFragment.TOUR_ID, tour.getId());
				edit_button.setOnClickListener(
						Navigation.createNavigateOnClickListener(R.id.toEditTour, b));
				edit_button.setVisibility(View.VISIBLE);
			}

			// Set places
			List<Place> places = tour.getPlaces();
			adapterCompleted.setPlaces(viewModel.getCompletedPlaces());
			adapterNoCompleted.setPlaces(viewModel.getNoCompletedPlaces());
			tour_num_places.setText(String.format(getString(R.string.num_places), places.size()));

			play_button.setOnClickListener(v -> alert.show());

		}
	}
}
