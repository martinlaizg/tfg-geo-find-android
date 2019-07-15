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
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.card.MaterialCardView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

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
	Location usrLocation;
	Location placeLocation;
	float distance;
	private PlayTourViewModel viewModel;
	private LocationManager locationManager;
	private AlertDialog questionDialog;

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
		viewModel.loadPlay(u.getId(), tour_id).observe(this, place -> {
			if(place == null) {
				Log.e(TAG(), "onViewCreated: Something went wrong");
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack(R.id.navTour, false);
				return;
			}

			// If has question build it
			if(place.getQuestion() != null && !place.getQuestion().isEmpty()) {
				createDialog(place);
			} else {
				questionDialog = null;
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

	@Override
	public void onLocationChanged(@NonNull Location location) {
		Log.d(TAG(), "onLocationChanged: ");
		usrLocation = location;

		// Set distance
		if(placeLocation != null) {
			distance = (int) usrLocation.distanceTo(placeLocation);
			if(distance > 1000f) {
				float newDistance = distance / 1000;
				newDistance = Math.round(newDistance * 100f) / 100f;
				place_distance
						.setText(getResources().getString(R.string.place_distance_km, newDistance));
				Log.d(TAG(), "updateView: distance=" + newDistance + "km");
			} else {
				place_distance.setText(
						getResources().getString(R.string.place_distance_m, (int) distance));
				Log.d(TAG(), "updateView: distance=" + distance + "m");
			}
			if(distance < DISTANCE_TO_COMPLETE) {
				locationManager.removeUpdates(this);
				Log.d(TAG(), "updateView: user arrive to the place");

				// If has question display
				if(questionDialog != null && !questionDialog.isShowing()) {
					questionDialog.show();
				} else {
					completePlace();
				}
				return;
			}
		}
		updateView();
	}

	private void completePlace() {
		// hide question dialog if exist and is showing
		if(questionDialog != null && questionDialog.isShowing()) questionDialog.dismiss();
		viewModel.completePlace(place.getId()).observe(this, place -> {
			if(place == null) {
				if(viewModel.tourIsCompleted()) {
					AlertDialog.Builder questionDialogBuilder = new AlertDialog.Builder(
							requireContext()).setTitle(R.string.tour_completed);
					questionDialogBuilder.setPositiveButton(R.string.ok,
					                                        (dialog, which) -> Navigation
							                                        .findNavController(
									                                        requireActivity(),
									                                        R.id.main_fragment_holder)
							                                        .popBackStack(R.id.navTour,
							                                                      false));
					questionDialogBuilder.show();
					return;
				}
				Toast.makeText(requireContext(), viewModel.getError().getMessage(),
				               Toast.LENGTH_SHORT).show();
			}
		});
		Log.d(TAG(), "updateView: Place done");
		Place p = viewModel.getNextPlace();
		if(p == null) {
			Log.e(TAG(), "completePlace: null nextPlace ");
			Toast.makeText(requireContext(), getString(R.string.something_went_wrong),
			               Toast.LENGTH_SHORT).show();
		} else {
			setPlace(p);
			if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED && requireActivity()
					.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
					PackageManager.PERMISSION_GRANTED) {
				Toast.makeText(requireContext(), R.string.rejected_location_access,
				               Toast.LENGTH_SHORT).show();
				return;
			}
			locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ,
			                                       LOC_DIST_REQ, this);
		}

	}

	private void setPlace(Place nextPlace) {
		place = nextPlace;
		placeLocation = new Location("");
		placeLocation.setLatitude(place.getLat());
		placeLocation.setLongitude(place.getLon());
		place_name.setText(place.getName());
		place_description.setText(place.getDescription());
		int numCompletedPlaces = viewModel.getPlay().getPlaces().size() + 1;
		int numPlaces = viewModel.getPlay().getTour().getPlaces().size();
		place_complete.setText(getResources().getQuantityString(R.plurals.place_number_number,
		                                                        numCompletedPlaces,
		                                                        numCompletedPlaces, numPlaces));
		updateView();
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

	private void createDialog(Place place) {
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(requireContext());
		View dialogView = getLayoutInflater()
				.inflate(R.layout.question_layout, new ConstraintLayout(requireContext()), false);
		TextView question = dialogView.findViewById(R.id.question);
		question.setText(place.getQuestion());
		List<MaterialCardView> cards = Arrays.asList(dialogView.findViewById(R.id.answer1_layout),
		                                             dialogView.findViewById(R.id.answer2_layout),
		                                             dialogView.findViewById(R.id.answer3_layout));
		List<TextView> texts = Arrays.asList(dialogView.findViewById(R.id.answer1),
		                                     dialogView.findViewById(R.id.answer2),
		                                     dialogView.findViewById(R.id.answer3));

		// Get random position to start
		int i = new Random().nextInt(cards.size());

		// Set correct answer
		texts.get(i).setText(place.getAnswer());
		cards.get(i).setOnClickListener(v -> completePlace());
		i++;
		i %= cards.size();
		// set second answer
		texts.get(i).setText(place.getAnswer2());
		cards.get(i).setOnClickListener(v -> showWrongAnswerToast());
		i++;
		i %= cards.size();
		texts.get(i).setText(place.getAnswer3());
		cards.get(i).setOnClickListener(v -> showWrongAnswerToast());

		dialogBuilder.setView(dialogView);
		questionDialog = dialogBuilder.create();
	}

	private void showWrongAnswerToast() {
		Toast.makeText(requireContext(), getString(R.string.wrong_answer), Toast.LENGTH_SHORT)
				.show();
	}
}
