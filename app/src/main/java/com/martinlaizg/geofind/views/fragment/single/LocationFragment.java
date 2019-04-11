package com.martinlaizg.geofind.views.fragment.single;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment
		extends Fragment {

	public static final String PLACE_ID = "PLACE_ID";

	@BindView(R.id.location_name)
	TextView location_name;
	@BindView(R.id.location_description)
	TextView location_description;
	@BindView(R.id.location_visits)
	TextView location_visits;
	@BindView(R.id.location_image)
	ImageView location_image;


	MapViewModel viewModel;
	private int place_id;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_location, container, false);
		ButterKnife.bind(this, view);
		Bundle b = getArguments();
		if (b != null) {
			place_id = b.getInt(PLACE_ID);
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(this).get(MapViewModel.class);

		viewModel.getPlace(place_id).observe(this, this::setPlace);
	}

	private void setPlace(Place place) {
		if (place != null) {
			location_name.setText(place.getName());
			location_description.setText(place.getDescription()); // TODO cambiar por el valor real
			location_visits.setText("2.594 TODO"); // TODO cambiar por el valor real
			location_image.setImageResource(R.drawable.default_map_image);
		}
	}
}
