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
import com.google.android.gms.maps.model.LatLng;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.TourViewModel;
import com.squareup.picasso.Picasso;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceFragment
		extends Fragment
		implements OnMapReadyCallback {

	public static final String PLACE_ID = "PLACE_ID";
	private static final float MAP_ZOOM = 14.5f;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.place_position)
	TextView place_position;
	@BindView(R.id.place_image)
	ImageView place_image;
	@BindView(R.id.place_map)
	MapView place_map;

	private int place_id;
	private GoogleMap googleMap;
	private TourViewModel viewModel;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
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

		viewModel.getPlace(place_id).observe(this, this::setPlace);
	}

	private void setPlace(Place place) {
		if(place != null) {
			place_name.setText(place.getName());
			place_description.setText(place.getDescription());

			if(place.getImage() != null && !place.getImage().isEmpty()) {
				Picasso.with(requireContext()).load(place.getImage()).into(place_image);
			} else {
				place_image.setImageResource(R.drawable.default_map_image);
			}

			int number = place.getOrder() + 1;
			int total = viewModel.getPlaces().size();
			place_position.setText(getResources()
					                       .getQuantityString(R.plurals.place_number_number, number,
					                                          number, total));
			if(googleMap != null) {
				LatLng position = place.getPosition();
				float latRand = (new Random().nextInt(20000) - 10000) / 3000000f;
				float lonRand = (new Random().nextInt(20000) - 10000) / 3000000f;
				position = new LatLng(position.latitude + latRand, position.longitude + lonRand);
				CameraUpdate cu = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM);
				googleMap.moveCamera(cu);
			}
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		googleMap.getUiSettings().setAllGesturesEnabled(false);
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		this.googleMap = googleMap;
	}
}
