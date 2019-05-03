package com.martinlaizg.geofind.views.fragment.play;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.navigation.Navigation;

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
import com.martinlaizg.geofind.data.access.database.entities.Play;

import org.jetbrains.annotations.NotNull;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayMapFragment
		extends PlayTourFragment
		implements OnMapReadyCallback {

	private static final String TAG = PlayMapFragment.class.getSimpleName();

	private static final float DISTANCE_TO_COMPLETE = 20;
	private final static float MAX_ZOOM = 18.6f;
	private static final float DISTANCE_TO_FIX_ZOOM = 100;
	private static final int MAP_PADDING = 200;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.place_complete)
	TextView place_complete;
	@BindView(R.id.place_distance)
	TextView place_distance;
	@BindView(R.id.map_type_button)
	MaterialButton map_type_button;
	@BindView(R.id.map_view)
	MapView map_view;
	private GoogleMap googleMap;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_tour, container, false);
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
		Play play = viewModel.getPlay();
		if(place != null) {
			place_name.setText(place.getName());
			place_description.setText(place.getDescription());
			int completed = play.getPlaces().size() + 1;
			int total = play.getTour().getPlaces().size();
			place_complete
					.setText(getResources().getString(R.string.tour_completenes, completed, total));

			if(googleMap != null && usrLocation != null) {

				// Set distance
				Location placeLocation = new Location("");
				placeLocation.setLatitude(place.getLat());
				placeLocation.setLongitude(place.getLon());
				Float distance = 0f;
				distance = usrLocation.distanceTo(placeLocation);
				Log.d(TAG, "updateView: distance=" + distance + "m");
				place_distance.setText(
						getResources().getString(R.string.place_distance, distance.intValue()));
				if(distance < DISTANCE_TO_COMPLETE) {

					Log.i(TAG, "updateView: user arrive to the place");
					viewModel.completePlace(place.getId()).observe(this, done -> {
						if(!done) {
							Toast.makeText(requireContext(), viewModel.getError().getMessage(),
							               Toast.LENGTH_SHORT).show();
							return;
						}
						Log.d(TAG, "updateView: Place done");
						Bundle b = new Bundle();
						b.putInt(TOUR_ID, viewModel.getTour().getId());
						Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
								.navigate(R.id.reload_play_map, b);
					});
				}

				// Move map camera
				LatLngBounds.Builder builder = new LatLngBounds.Builder();
				builder.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude()));
				builder.include(place.getPosition());
				CameraUpdate cu;
				LatLngBounds cameraPosition = builder.build();
				if(distance < DISTANCE_TO_FIX_ZOOM) {
					cu = CameraUpdateFactory.newLatLngZoom(cameraPosition.getCenter(), MAX_ZOOM);
				} else {
					cu = CameraUpdateFactory.newLatLngBounds(cameraPosition, MAP_PADDING);
				}
				googleMap.getUiSettings().setMyLocationButtonEnabled(true);
				googleMap.animateCamera(cu);
				Log.d(TAG, "updateView: zoom=" + googleMap.getCameraPosition().zoom);

				// Add place marker
				googleMap.addMarker(new MarkerOptions().position(place.getPosition()));
			}
		}
	}
}
