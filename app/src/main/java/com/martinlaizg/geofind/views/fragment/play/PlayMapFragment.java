package com.martinlaizg.geofind.views.fragment.play;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayMapFragment
		extends PlayTourFragment
		implements OnMapReadyCallback {

	private static final String TAG = PlayMapFragment.class.getSimpleName();

	private final static float MAX_ZOOM = 18.6f;
	private static final float DISTANCE_TO_FIX_ZOOM = 100;
	private static final int MAP_PADDING = 200;

	@BindView(R.id.map_type_button)
	MaterialButton map_type_button;
	@BindView(R.id.map_view)
	MapView map_view;

	private GoogleMap googleMap;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_map, container, false);
		ButterKnife.bind(this, view);
		map_view.onCreate(savedInstanceState);
		map_view.onResume();
		map_view.getMapAsync(this);

		String[] options = getResources().getStringArray(R.array.mapTypes);
		int[] mapTypes = {GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_SATELLITE};
		map_type_button.setOnClickListener(v -> {
			AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
			builder.setTitle(getResources().getString(R.string.map_type));
			builder.setItems(options, (dialog, item) -> googleMap.setMapType(mapTypes[item]));
			AlertDialog alert = builder.create();
			alert.show();
		});
		return view;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
						PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
					                   Manifest.permission.ACCESS_FINE_LOCATION},
			                   PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		this.googleMap.setMyLocationEnabled(true);
		this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		this.googleMap.getUiSettings().setMapToolbarEnabled(false);
		updateView();
	}

	@Override
	protected String TAG() {
		return TAG;
	}

	protected void updateView() {

		if(googleMap != null && usrLocation != null) {

			CameraUpdate cu = null;
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			builder.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude()));
			if(place != null) {
				// Set distance
				place_distance.setText(
						getResources().getString(R.string.place_distance, distance.intValue()));

				// Add place to camera update
				builder.include(place.getPosition());

				// Add place marker
				googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
			}
			LatLngBounds cameraPosition = builder.build();
			if(place == null || distance < DISTANCE_TO_FIX_ZOOM) {
				cu = CameraUpdateFactory.newLatLngZoom(cameraPosition.getCenter(), MAX_ZOOM);
			} else {
				cu = CameraUpdateFactory.newLatLngBounds(cameraPosition, MAP_PADDING);
			}
			googleMap.getUiSettings().setMyLocationButtonEnabled(true);
			googleMap.animateCamera(cu);
			Log.d(TAG, "updateView: zoom=" + googleMap.getCameraPosition().zoom);

		}
	}

	@Override
	void setUpView() {
		place_name.setText(place.getName());
		place_description.setText(place.getDescription());
		int numCompletedPlaces = viewModel.getPlay().getPlaces().size() + 1;
		int numPlaces = viewModel.getPlay().getTour().getPlaces().size();
		place_complete.setText(getResources().getString(R.string.tour_completenes, numCompletedPlaces, numPlaces));

	}
}
