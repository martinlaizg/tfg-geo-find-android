package com.martinlaizg.geofind;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayTourFragment
		extends Fragment
		implements OnMapReadyCallback {

	public static final String TOUR_ID = "TOUR_ID";

	private static final String TAG = PlayTourFragment.class.getSimpleName();
	private static final int MAP_PADDING = 80;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.map_view)
	MapView map_view;

	private PlayTourViewModel viewModel;

	private GoogleMap googleMap;
	private Location userLocation;
	private Place place;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_tour, container, false);
		ButterKnife.bind(this, view);
		map_view.onCreate(savedInstanceState);
		map_view.onResume();
		map_view.getMapAsync(this);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(PlayTourViewModel.class);
		Bundle b = getArguments();
		int tour_id = 0;
		if (b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		User u = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()));
		viewModel.loadPlay(u.getId(), tour_id).observe(requireActivity(), play -> {
			place = viewModel.getNextPlace();
			place_name.setText(place.getName());
			place_description.setText(place.getDescription());
			setPlace();
		});
	}

	private void setPlace() {
		if (googleMap != null) {
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			if (place != null) builder.include(place.getPosition());
			if (userLocation != null) builder.include(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
			CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING);

			if (place != null) googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
			googleMap.getUiSettings().setMyLocationButtonEnabled(false);
			googleMap.getUiSettings().setMapToolbarEnabled(false);
			googleMap.moveCamera(cu);
		}
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		if (checkPermissions()) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
		}
		googleMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
		userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		setPlace();
	}

	private boolean checkPermissions() {
		return requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				googleMap.setMyLocationEnabled(true);
				LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
				userLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				setPlace();
			}
		}

	}
}
