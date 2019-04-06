package com.martinlaizg.geofind.views.fragment.creator;


import android.Manifest;
import android.annotation.SuppressLint;
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

import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;


public class CreateLocationFragment
		extends Fragment
		implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

	public static final String LOC_POSITION = "LOC_POSITION";
	private static final int CAMERA_UPDATE_ZOOM = 15;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;

	@BindView(R.id.new_location_name_layout)
	TextInputLayout new_location_name;
	@BindView(R.id.new_location_description_layout)
	TextInputLayout new_location_description;
	@BindView(R.id.alert_no_locaiton_text)
	TextView alert_no_location_text;
	@BindView(R.id.create_location)
	Button create_button;
	@BindView(R.id.new_location_map_view)
	MapView new_location_map_view;

	private MapCreatorViewModel viewModel;
	private MarkerOptions marker;
	private GoogleMap gMap;
	private int position;

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
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(MapCreatorViewModel.class);
		Bundle b = getArguments();
		if (b != null) {
			position = b.getInt(LOC_POSITION);
			com.martinlaizg.geofind.data.access.database.entity.Location l = viewModel.getCreatedLocations().get(position);
			Objects.requireNonNull(new_location_name.getEditText()).setText(l.getName());
			Objects.requireNonNull(new_location_description.getEditText()).setText(l.getDescription());
			onMapLongClick(new LatLng(Float.valueOf(l.getLat()), Float.valueOf(l.getLon())));
		} else {
			position = viewModel.getCreatedLocations().size();
		}
		create_button.setOnClickListener(this);
		if (marker != null) create_button.setText(R.string.update_location);

	}

	@Override
	public void onClick(View v) {
		alert_no_location_text.setVisibility(View.GONE);
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
				alert_no_location_text.setVisibility(View.VISIBLE);
				return;
			}
		} catch (NullPointerException ex) {
			Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
			return;
		}

		String name = new_location_name.getEditText().getText().toString().trim();
		String description = new_location_description.getEditText().getText().toString().trim();

		viewModel.addLocation(name, description, String.valueOf(marker.getPosition().latitude), String.valueOf(marker.getPosition().longitude), position);

		Navigation.findNavController(getActivity(), R.id.main_fragment_holder).popBackStack();
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		gMap = googleMap;
		if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			Toast.makeText(getActivity(), getString(R.string.no_location_permissions), Toast.LENGTH_SHORT).show();
			return;
		}
		setLocation();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				setLocation();
			}
		}
	}

	@SuppressLint("MissingPermission")
	private void setLocation() {
		Location usrLocation = getLastKnownLocation();
		if (gMap != null) {
			gMap.setMyLocationEnabled(true);
			gMap.setOnMapLongClickListener(this);
			gMap.getUiSettings().setMyLocationButtonEnabled(true);
			gMap.getUiSettings().setMapToolbarEnabled(false);
			gMap.getUiSettings().setTiltGesturesEnabled(false);

			LatLng usrLatLng = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());
			gMap.clear();
			if (marker != null) {
				gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker.getPosition(), CAMERA_UPDATE_ZOOM));
				gMap.addMarker(marker);
			} else {
				gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(usrLatLng, CAMERA_UPDATE_ZOOM));
			}
		}
	}

	@SuppressLint("MissingPermission")
	private Location getLastKnownLocation() {
		LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(LOCATION_SERVICE);
		List<String> providers = locationManager.getProviders(true);
		Location bestLocation = null;
		for (String provider : providers) {
			Location l = locationManager.getLastKnownLocation(provider);
			if (l == null) {
				continue;
			}
			if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}
		return bestLocation;
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		alert_no_location_text.setVisibility(View.GONE);
		MarkerOptions m = new MarkerOptions().position(latLng);
		if (gMap != null) {
			gMap.clear();
			gMap.addMarker(m);
		}
		marker = m;

	}
}
