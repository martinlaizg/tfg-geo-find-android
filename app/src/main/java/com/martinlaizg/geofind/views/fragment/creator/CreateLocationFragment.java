package com.martinlaizg.geofind.views.fragment.creator;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.views.viewmodel.MapCreatorViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateLocationFragment
		extends Fragment
		implements View.OnClickListener, OnMapReadyCallback {

	private static final int CAMERA_UPDATE_ZOOM = 15;
	private static final String MAP_NAME = "MAP_NAME";
	private static final String MAP_DESCRIPTION = "MAP_DESCRIPTION";

	@BindView(R.id.new_location_name_layout)
	TextInputLayout new_location_name;
	@BindView(R.id.new_location_description_layout)
	TextInputLayout new_location_description;
	@BindView(R.id.alert_no_locaiton_text)
	TextView alert_no_locaiton_text;

	@BindView(R.id.create_location)
	Button create_button;

	@BindView(R.id.new_location_map_view)
	MapView new_location_map_view;

	private MapCreatorViewModel viewModel;
	private MarkerOptions marker;

	@Override
	public View onCreateView(
			@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_locations, container, false);
		ButterKnife.bind(this, view);
		new_location_map_view.onCreate(savedInstanceState);
		new_location_map_view.onResume();
		new_location_map_view.getMapAsync(this);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);
		create_button.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		alert_no_locaiton_text.setVisibility(View.GONE);
		try {
			if (Objects.requireNonNull(new_location_name.getEditText()).getText().toString().trim().isEmpty()) {
				new_location_name.setError(getString(R.string.required_name));
				return;
			}
			if (new_location_name.getEditText().getText().toString().length() > getResources().getInteger(R.integer.max_name_length)) {
				new_location_name.setError(getString(R.string.you_oversized));
				return;
			}
			new_location_name.setError("");
			if (Objects.requireNonNull(new_location_description.getEditText()).getText().toString().trim().isEmpty()) {
				new_location_description.setError(getString(R.string.required_description));
				return;
			}
			if (new_location_description.getEditText().getText().toString().length() > getResources().getInteger(R.integer.max_description_length)) {
				new_location_description.setError(getString(R.string.you_oversized));
				return;
			}
			new_location_description.setError("");
			if (marker == null) {
				alert_no_locaiton_text.setVisibility(View.VISIBLE);
				return;
			}
		} catch (NullPointerException ex) {
			Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		String name = new_location_name.getEditText().getText().toString().trim();
		String description = new_location_description.getEditText().getText().toString().trim();
		viewModel.addLocation(name, description, String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude));

		Navigation.findNavController(getActivity(), R.id.main_fragment_holder).popBackStack();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {

		LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
		if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			Toast.makeText(getActivity(), getString(R.string.no_location_permissions), Toast.LENGTH_SHORT).show();
			return;
		}
		Location usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
			@Override
			public void onMapLongClick(LatLng latLng) {
				alert_no_locaiton_text.setVisibility(View.GONE);
				MarkerOptions m = new MarkerOptions().position(latLng);
				googleMap.clear();
				googleMap.addMarker(m);
				marker = m;
			}
		});
		googleMap.setMyLocationEnabled(true);
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.getUiSettings().setZoomControlsEnabled(true);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		googleMap.getUiSettings().setTiltGesturesEnabled(false);
		googleMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);

		LatLng usrLatLng = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(usrLatLng, CAMERA_UPDATE_ZOOM);
		googleMap.animateCamera(cameraUpdate);
	}

}
