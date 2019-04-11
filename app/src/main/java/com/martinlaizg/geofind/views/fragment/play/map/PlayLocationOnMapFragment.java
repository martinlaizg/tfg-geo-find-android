package com.martinlaizg.geofind.views.fragment.play.map;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

@Deprecated
public class PlayLocationOnMapFragment
		extends Fragment
		implements OnMapReadyCallback, LocationListener {

	static final String LOCATION_ID = "LOCATION_ID";
	private static final String TAG = PlayLocationOnMapFragment.class.getSimpleName();
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;
	// Minimum time (ms) between place updates
	private static final long MIN_MS_LOC_UPDATE = 500;
	// Minimum distance (meters) between place updates
	private static final float MIN_METERS_LOC_UPDATE = 1;
	// Padding (px) between markers and border in location_map_view
	private static final int MAP_PADDING = 100;

	@BindView(R.id.location_name)
	TextView location_name;
	@BindView(R.id.location_description)
	TextView location_description;
	@BindView(R.id.navigate_button)
	MaterialButton navigate_button;

	@BindView(R.id.location_map_view)
	MapView location_map_view;

	private Place place;
	private GoogleMap gMap;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_location_on_map, container, false);
		ButterKnife.bind(this, view);
		location_map_view.onCreate(savedInstanceState);
		location_map_view.onResume();
		location_map_view.getMapAsync(this);
		navigate_button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String uri = String.format(Locale.ENGLISH, "google.navigation:q=%f,%f", place.getPosition().latitude, place.getPosition().longitude);
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
				requireActivity().startActivity(intent);
			}
		});
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		Bundle b = getArguments();
		String loc_id = "";
		if (b != null) {
			loc_id = b.getString(LOCATION_ID);
		}
		MapViewModel viewModel = ViewModelProviders.of(requireActivity()).get(MapViewModel.class);

		location_name.setText(place.getName());
		location_description.setText(place.getDescription());
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		gMap = googleMap;
		if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
		}
		gMap.setMyLocationEnabled(true);
		LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
		updateLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
		}
		LocationManager locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_MS_LOC_UPDATE, MIN_METERS_LOC_UPDATE, this);
		updateLocation(locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER));
	}

	private void updateLocation(android.location.Location usrLocation) {
		Log.i(TAG, "Place updated");
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(place.getPosition());
		builder.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude()));
		LatLngBounds bounds = builder.build();
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING);

		if (gMap == null) return;
		gMap.addMarker(new MarkerOptions().position(place.getPosition()));
		gMap.getUiSettings().setMyLocationButtonEnabled(false);
		gMap.getUiSettings().setMapToolbarEnabled(false);
		gMap.moveCamera(cu);
	}


	@Override
	public void onLocationChanged(android.location.Location usrLocation) {
		updateLocation(usrLocation);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {

	}

	@Override
	public void onProviderEnabled(String provider) {

	}

	@Override
	public void onProviderDisabled(String provider) {

	}

}
