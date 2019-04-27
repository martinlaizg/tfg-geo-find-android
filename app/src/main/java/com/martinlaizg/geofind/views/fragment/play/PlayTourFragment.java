package com.martinlaizg.geofind.views.fragment.play;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
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
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayTourFragment
		extends Fragment
		implements OnMapReadyCallback, LocationListener {

	public static final String TOUR_ID = "TOUR_ID";

	private static final String TAG = PlayTourFragment.class.getSimpleName();
	private static final int MAP_PADDING = 80;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;
	private static final long LOC_TIME_REQ = 500;
	private static final float LOC_DIST_REQ = 5;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.map_view)
	MapView map_view;

	private PlayTourViewModel viewModel;

	private GoogleMap googleMap;
	private Place place;
	private LocationManager locationManager;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, "onRequestPermissionsResult: success");
				onMapReady(googleMap);
				return;
			}
			Log.d(TAG, "onRequestPermissionsResult: deny");
		}
	}

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
		if(b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		User u = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()));
		viewModel.loadPlay(u.getId(), tour_id).observe(requireActivity(), play -> {
			place = viewModel.getNextPlace();
			place_name.setText(place.getName());
			place_description.setText(place.getDescription());
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume: check location permissions");
		locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
			                   PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		//		locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ, LOC_DIST_REQ, this);
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause: remove location updates");
		locationManager.removeUpdates(this);
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		Log.i(TAG, "onLocationChanged: ");
		if(place != null && googleMap != null) {
			updateMap(googleMap, location, place);
			// Disable updates
			locationManager.removeUpdates(this);
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(TAG, "onStatusChanged: ");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.i(TAG, "onProviderEnabled: ");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.i(TAG, "onProviderDisabled: ");
	}

	@SuppressLint("MissingPermission")
	private void updateMap(@NonNull GoogleMap googleMap, @NonNull Location userLocation, @NonNull Place place) {
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		builder.include(place.getPosition());
		builder.include(new LatLng(userLocation.getLatitude(), userLocation.getLongitude()));
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING);

		googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
		googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		googleMap.getUiSettings().setMapToolbarEnabled(false);
		googleMap.animateCamera(cu);
		googleMap.setMyLocationEnabled(true);
	}
}
