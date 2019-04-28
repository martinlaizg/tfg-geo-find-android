package com.martinlaizg.geofind.views.fragment.play;

import android.Manifest;
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

	private static final int MAP_PADDING = 200;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;
	private static final long LOC_TIME_REQ = 500;
	private static final float LOC_DIST_REQ = 5;
	private static final float DISTANCE_TO_COMPLETE = 20;
	private final static float MAX_ZOOM = 19.5f;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.place_complete)
	TextView place_complete;
	@BindView(R.id.place_distance)
	TextView place_distance;
	@BindView(R.id.map_view)
	MapView map_view;

	private PlayTourViewModel viewModel;
	private GoogleMap googleMap;
	private Place place;
	private LocationManager locationManager;
	private Location usrLocation;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if(requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG, "onRequestPermissionsResult: success");
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
			int completed = play.getPlaces().size() + 1;
			int total = play.getTour().getPlaces().size();
			place_complete.setText(getResources().getString(R.string.tour_completenes, completed, total));
			updateMap();
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG, "onResume: check location permissions");
		locationManager = (LocationManager) requireActivity().getSystemService(Context.LOCATION_SERVICE);
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i(TAG, "onResume: start request location updates");
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ, LOC_DIST_REQ, this);
		updateMap();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG, "onPause: remove location updates");
		locationManager.removeUpdates(this);
	}

	private void updateMap() {
		if(googleMap != null && place != null && usrLocation != null) {

			// Set distance
			Location placeLocation = new Location("");
			placeLocation.setLatitude(place.getLat());
			placeLocation.setLongitude(place.getLon());
			Float distance = 0f;
			distance = usrLocation.distanceTo(placeLocation);
			Log.i(TAG, "updateMap: " + distance + "m");
			place_distance.setText(getResources().getString(R.string.place_distance, distance.intValue()));
			if(distance < DISTANCE_TO_COMPLETE) {
				Log.i(TAG, "updateMap: user arrive to the place");
			}

			// Move map camera
			LatLngBounds.Builder builder = new LatLngBounds.Builder();
			builder.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude()));
			builder.include(place.getPosition());
			CameraUpdate cu;
			cu = CameraUpdateFactory.newLatLngBounds(builder.build(), MAP_PADDING);
			googleMap.setOnCameraMoveStartedListener(i -> {
				if(googleMap.getCameraPosition().zoom > MAX_ZOOM) { // adjust maximum zoom
					CameraUpdate c = CameraUpdateFactory.newLatLngZoom(googleMap.getCameraPosition().target, MAX_ZOOM);
					googleMap.animateCamera(c);
				}
			});
			googleMap.animateCamera(cu);

			// Add place marker
			googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
		}
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		this.googleMap.setMyLocationEnabled(true);
		this.googleMap.getUiSettings().setAllGesturesEnabled(false);
		this.googleMap.getUiSettings().setMyLocationButtonEnabled(false);
		this.googleMap.getUiSettings().setMapToolbarEnabled(false);
		updateMap();
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		Log.i(TAG, "onLocationChanged: ");
		usrLocation = location;
		updateMap();
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
}
