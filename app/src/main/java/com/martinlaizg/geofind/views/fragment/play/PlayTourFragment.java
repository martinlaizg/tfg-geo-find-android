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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel;

import butterknife.BindView;

abstract class PlayTourFragment
		extends Fragment
		implements LocationListener {

	public static final String TOUR_ID = "TOUR_ID";

	static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;

	private static final float DISTANCE_TO_COMPLETE = 15;
	private static final long LOC_TIME_REQ = 200;
	private static final float LOC_DIST_REQ = 2;

	@BindView(R.id.place_name)
	TextView place_name;
	@BindView(R.id.place_description)
	TextView place_description;
	@BindView(R.id.place_complete)
	TextView place_complete;
	@BindView(R.id.place_distance)
	TextView place_distance;

	Place place;
	private PlayTourViewModel viewModel;
	Location usrLocation;
	Location placeLocation;
	Float distance;

	private LocationManager locationManager;

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if(requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION && permissions.length >= 2) {
			if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
					grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG(), "onRequestPermissionsResult: success");
			}
			Log.d(TAG(), "onRequestPermissionsResult: deny");
		}
	}

	protected abstract String TAG();

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(PlayTourViewModel.class);
		Bundle b = getArguments();
		int tour_id = 0;
		if(b != null) {
			tour_id = b.getInt(TOUR_ID);
		}
		User u = Preferences
				.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()));
		viewModel.loadPlay(u.getId(), tour_id).observe(requireActivity(), place -> {
			if(place == null) {
				Log.i(TAG(), "onViewCreated: tour completed");
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.navigate(R.id.toCompleteTour);
				return;
			}
			setPlace(place);
		});
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.i(TAG(), "onResume: check location permissions");
		locationManager = (LocationManager) requireActivity()
				.getSystemService(Context.LOCATION_SERVICE);
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
						PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
					                   Manifest.permission.ACCESS_FINE_LOCATION},
			                   PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			return;
		}
		usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		Log.i(TAG(), "onResume: start request location updates");
		locationManager
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ, LOC_DIST_REQ,
				                        this);
		updateView();
	}

	@Override
	public void onPause() {
		super.onPause();
		Log.i(TAG(), "onPause: delete location updates");
		locationManager.removeUpdates(this);
	}

	abstract void updateView();

	private void setPlace(Place nextPlace) {
		place = nextPlace;
		placeLocation = new Location("");
		placeLocation.setLatitude(place.getLat());
		placeLocation.setLongitude(place.getLon());
		place_name.setText(place.getName());
		place_description.setText(place.getDescription());
		int numCompletedPlaces = viewModel.getPlay().getPlaces().size() + 1;
		int numPlaces = viewModel.getPlay().getTour().getPlaces().size();
		place_complete.setText(
				getResources().getString(R.string.tour_completeness, numCompletedPlaces, numPlaces));
	}

	@Override
	public void onLocationChanged(@NonNull Location location) {
		Log.d(TAG(), "onLocationChanged: ");
		usrLocation = location;

		// Set distance
		if(placeLocation != null) {
			distance = usrLocation.distanceTo(placeLocation);
			place_distance.setText(
					getResources().getString(R.string.place_distance, distance.intValue()));
			Log.d(TAG(), "updateView: distance=" + distance + "m");
			if(distance < DISTANCE_TO_COMPLETE) {
				Log.d(TAG(), "updateView: user arrive to the place");
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.navigate(R.id.toCompletePlace);
				viewModel.completePlace(place.getId()).observe(this, done -> {
					if(!done) {
						Toast.makeText(requireContext(), viewModel.getError().getMessage(),
						               Toast.LENGTH_SHORT).show();
						return;
					}
					Log.d(TAG(), "updateView: Place done");
					Place p = viewModel.getNextPlace();
					if(p == null) {
						if(viewModel.getError().getType() == ErrorType.COMPLETED) {
							Navigation
									.findNavController(requireActivity(), R.id.main_fragment_holder)
									.navigate(R.id.toCompleteTour);
						}
					} else {
						setPlace(p);
					}
				});
			}
			updateView();
		}
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i(TAG(), "onStatusChanged: ");
	}

	@Override
	public void onProviderEnabled(String provider) {
		Log.i(TAG(), "onProviderEnabled: ");
	}

	@Override
	public void onProviderDisabled(String provider) {
		Log.i(TAG(), "onProviderDisabled: ");
	}

}
