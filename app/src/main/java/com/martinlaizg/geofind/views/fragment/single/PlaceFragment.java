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

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.TourViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceFragment
		extends Fragment
		implements OnMapReadyCallback {

	public static final String PLACE_ID = "PLACE_ID";
	private static final float MAP_ZOOM = 14.5f;

	@BindView(R.id.place_name)
	TextView location_name;
	@BindView(R.id.place_description)
	TextView location_description;
	@BindView(R.id.place_position)
	TextView place_position;
	@BindView(R.id.place_image)
	ImageView location_image;
	@BindView(R.id.place_map)
	MapView place_map;

	private int place_id;
	private GoogleMap googleMap;
	private TourViewModel viewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_place, container, false);
		ButterKnife.bind(this, view);
		place_map.onCreate(savedInstanceState);
		place_map.onResume();
		place_map.getMapAsync(this);
		Bundle b = getArguments();
		if(b != null) {
			place_id = b.getInt(PLACE_ID);
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel.class);

		viewModel.loadPlace(place_id).observe(this, this::setPlace);
	}

	private void setPlace(Place place) {
		if(place != null) {
			location_name.setText(place.getName());
			location_description.setText(place.getDescription());
			location_image.setImageResource(R.drawable.default_map_image);
			int number = place.getOrder() + 1;
			int total = viewModel.getPlaces().size();
			place_position.setText(getResources().getString(R.string.place_completeness, number, total));
			if(googleMap != null) {
				googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
				CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(place.getPosition(), MAP_ZOOM);
				googleMap.moveCamera(cu);
			}
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap.getUiSettings().setAllGesturesEnabled(false);
		this.googleMap = googleMap;
	}
}
