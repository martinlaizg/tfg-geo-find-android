package com.martinlaizg.geofind.views.fragment.play.map;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationManager;
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
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

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

public class PlayMapOnMapFragment
		extends Fragment
		implements OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {

	private static final int MAP_PADDING = 80;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;
	private static final String TAG = PlayMapOnMapFragment.class.getSimpleName();
	@BindView(R.id.map_name)
	TextView map_name;
	@BindView(R.id.map_description)
	TextView map_description;

	@BindView(R.id.map_view)
	MapView map_view;
	private MapViewModel viewModel;
	private GoogleMap gMap;

	@Override
	public View onCreateView(
			@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_map_on_map, container, false);
		ButterKnife.bind(this, view);
		map_view.onCreate(savedInstanceState);
		map_view.onResume();
		map_view.getMapAsync(this);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
		Map map = viewModel.getMap();
		map_name.setText(map.getName());
		map_description.setText(map.getDescription());
	}


	@Override
	public void onMapReady(GoogleMap googleMap) {
		// TODO: refactor
		gMap = googleMap;
		if (Objects.requireNonNull(getActivity()).checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
				getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		setLocations();
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
		if (requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if (permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				setLocations();
			}
		}

	}

	@SuppressLint("MissingPermission")
	private void setLocations() {
		List<Location> locs = viewModel.getLocations();
		LatLngBounds.Builder builder = new LatLngBounds.Builder();
		for (Location l : locs) { // Add the locations to the builder
			MarkerOptions m = new MarkerOptions().position(l.getLatLng()).title(l.getName());
			gMap.addMarker(m);
			builder.include(m.getPosition());
		}

		LocationManager locationManager = (LocationManager) Objects.requireNonNull(getActivity()).getSystemService(Context.LOCATION_SERVICE);
		android.location.Location usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		builder.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude())); // Add the user location to the builder
		gMap.setMyLocationEnabled(true);
		gMap.getUiSettings().setMyLocationButtonEnabled(false);
		gMap.getUiSettings().setMapToolbarEnabled(false);
		gMap.getUiSettings().setTiltGesturesEnabled(false);
		gMap.setOnInfoWindowClickListener(this);

		LatLngBounds bounds = builder.build();
		CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING);
		gMap.moveCamera(cu);
	}

	@Override
	public void onInfoWindowClick(Marker marker) {
		String name = marker.getTitle();
		Location l = viewModel.getLocationByName(name);
		if (l == null) {
			Log.e(TAG, "onInfoWindowClick: location no exist", null);
			return;
		}
		Bundle b = new Bundle();
		b.putString(PlayLocationOnMapFragment.LOCATION_ID, l.getId());
		Navigation.findNavController(getActivity(), R.id.main_fragment_holder).navigate(R.id.toPlayLocationOnMap, b);
	}
}
