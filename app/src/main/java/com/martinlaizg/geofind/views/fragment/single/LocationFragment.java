package com.martinlaizg.geofind.views.fragment.single;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.TourViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment
		extends Fragment {

	public static final String PLACE_ID = "PLACE_ID";

	@BindView(R.id.place_name)
	TextView location_name;
	@BindView(R.id.place_description)
	TextView location_description;
	@BindView(R.id.place_image)
	ImageView location_image;

	private int place_id;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_place, container, false);
		ButterKnife.bind(this, view);
		Bundle b = getArguments();
		if(b != null) {
			place_id = b.getInt(PLACE_ID);
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		TourViewModel viewModel = ViewModelProviders.of(this).get(TourViewModel.class);

		viewModel.loadPlace(place_id).observe(this, this::setPlace);
	}

	private void setPlace(Place place) {
		if(place != null) {
			location_name.setText(place.getName());
			location_description.setText(place.getDescription());
			location_image.setImageResource(R.drawable.default_map_image);
		}
	}
}
