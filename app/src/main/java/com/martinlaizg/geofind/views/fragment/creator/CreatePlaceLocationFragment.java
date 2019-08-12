package com.martinlaizg.geofind.views.fragment.creator;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.button.MaterialButton;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.Context.LOCATION_SERVICE;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

public class CreatePlaceLocationFragment
		extends Fragment
		implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

	private static final String TAG = CreatePlaceLocationFragment.class.getSimpleName();

	private static final float ZOOM = 15;
	private static final int RC_LOCATION = 123;

	@BindView(R.id.map_view)
	MapView map_view;
	@BindView(R.id.create_place_location_button)
	MaterialButton create_place_location_button;

	private CreatorViewModel viewModel;
	private GoogleMap googleMap;
	private LocationManager locationManager;

	private Location usrLocation;
	private Location placeLocation;

	@SuppressLint("MissingPermission")
	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if(requestCode == RC_LOCATION) {
			if(grantResults.length > 0 && grantResults[0] == PERMISSION_GRANTED) {
				if(googleMap != null) {
					usrLocation = locationManager
							.getLastKnownLocation(LocationManager.GPS_PROVIDER);
					setMarker();
				}
			}
		}
	}

	private void setMarker() {
		if(googleMap == null) return;

		LatLng latLng;
		this.googleMap.clear();

		if(placeLocation != null) {
			// Use the place location
			latLng = new LatLng(placeLocation.getLatitude(), placeLocation.getLongitude());
			this.googleMap.addMarker(new MarkerOptions().position(latLng));
		} else {
			// Use the user location
			if(usrLocation == null) {
				// Check location permissions
				if(requireActivity().checkSelfPermission(ACCESS_FINE_LOCATION) !=
						PERMISSION_GRANTED &&
						requireActivity().checkSelfPermission(ACCESS_COARSE_LOCATION) !=
								PERMISSION_GRANTED) {
					// Request location permissions
					requireActivity().requestPermissions(
							new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION},
							RC_LOCATION);
					return;
				}
				locationManager = (LocationManager) requireActivity()
						.getSystemService(LOCATION_SERVICE);
				usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
				if(usrLocation == null) {
					Log.e(TAG, "setMarker: fail to get last known location (GPS)");
					return;
				}
			}
			latLng = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());
		}

		this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM));
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_place_location, container, false);
		ButterKnife.bind(this, view);
		map_view.onCreate(savedInstanceState);
		map_view.onResume();
		map_view.getMapAsync(this);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		placeLocation = viewModel.getPlace().getLocation();
		setMarker();
		create_place_location_button.setOnClickListener(
				v -> Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack());
	}

	@SuppressLint("MissingPermission")
	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		this.googleMap.setPadding(0, 0, 0, create_place_location_button.getHeight());
		this.googleMap.setOnMapLongClickListener(this);
		this.googleMap.setMyLocationEnabled(true);
		this.googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		this.googleMap.getUiSettings().setAllGesturesEnabled(true);
		setMarker();
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		googleMap.clear();
		googleMap.addMarker(new MarkerOptions().position(latLng));
		viewModel.getPlace().setPosition(latLng);
	}
}
