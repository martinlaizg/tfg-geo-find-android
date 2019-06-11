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
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.LOCATION_SERVICE;

public class CreatePlaceFragment
		extends Fragment
		implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

	public static final String PLACE_POSITION = "PLACE_POSITION";

	private static final String TAG = CreatePlaceFragment.class.getSimpleName();
	private static final int CAMERA_UPDATE_ZOOM = 15;
	private static final int PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1;

	@BindView(R.id.new_place_name_layout)
	TextInputLayout new_place_name;
	@BindView(R.id.new_place_description_layout)
	TextInputLayout new_place_description;
	@BindView(R.id.alert_no_place_text)
	TextView alert_no_place_text;
	@BindView(R.id.create_place)
	Button create_button;
	@BindView(R.id.new_place_map_view)
	MapView new_place_map_view;

	@BindView(R.id.question_switch)
	Switch question_switch;
	@BindView(R.id.question_layout)
	ConstraintLayout question_layout;
	@BindView(R.id.new_place_question)
	TextInputLayout new_place_question;
	@BindView(R.id.new_place_correct_answer)
	TextInputLayout new_place_correct_answer;
	@BindView(R.id.new_place_answer_2)
	TextInputLayout new_place_answer_2;
	@BindView(R.id.new_place_answer_3)
	TextInputLayout new_place_answer_3;

	private CreatorViewModel viewModel;
	private MarkerOptions marker;
	private GoogleMap gMap;
	private Place place;

	@Override
	public void onClick(View v) {
		alert_no_place_text.setVisibility(View.GONE);
		if(!checkFields()) return;

		viewModel.setPlace(place);
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
	}

	private boolean checkFields() {
		String placeName = Objects.requireNonNull(new_place_name.getEditText()).getText().toString()
				.trim();
		if(placeName.isEmpty()) {
			new_place_name.setError(getString(R.string.required_name));
			return false;
		}
		if(placeName.length() > getResources().getInteger(R.integer.max_name_length)) {
			new_place_name.setError(getString(R.string.text_too_long));
			return false;
		}
		new_place_name.setError("");
		String placeDescription = Objects.requireNonNull(new_place_description.getEditText())
				.getText().toString().trim();
		if(placeDescription.isEmpty()) {
			new_place_description.setError(getString(R.string.required_description));
			return false;
		}
		if(placeDescription.length() >
				getResources().getInteger(R.integer.max_description_length)) {
			new_place_description.setError(getString(R.string.text_too_long));
			return false;
		}
		new_place_description.setError("");
		if(marker == null) {
			alert_no_place_text.setVisibility(View.VISIBLE);
			return false;
		}

		boolean question = getQuestion();
		if(!question) return false;

		place.setName(placeName);
		place.setDescription(placeDescription);
		place.setPosition(marker.getPosition());

		return true;
	}

	private boolean getQuestion() {
		boolean withQuestion = question_switch.isChecked();
		if(!withQuestion) return true;

		// Check the question
		String question = Objects.requireNonNull(new_place_question.getEditText()).getText()
				.toString().trim();
		if(question.isEmpty()) {
			new_place_question.setError(getString(R.string.required_question));
			return false;
		}

		// Check the correct answer
		String correctAnswer = Objects.requireNonNull(new_place_correct_answer.getEditText())
				.getText().toString().trim();
		if(correctAnswer.isEmpty()) {
			new_place_correct_answer.setError(getString(R.string.required_answer));
			return false;
		}

		// Check the second answer
		String secondAnswer = Objects.requireNonNull(new_place_answer_2.getEditText()).getText()
				.toString().trim();
		if(secondAnswer.isEmpty()) {
			new_place_answer_2.setError(getString(R.string.required_answer));
			return false;
		}

		// Check the third answer
		String thirdAnswer = Objects.requireNonNull(new_place_answer_3.getEditText()).getText()
				.toString().trim();
		if(thirdAnswer.isEmpty()) {
			new_place_answer_3.setError(getString(R.string.required_answer));
			return false;
		}

		if(secondAnswer.equals(thirdAnswer)) {
			new_place_answer_3.setError(getString(R.string.answer_repeated));
			return false;
		}

		// Set the values
		place.setQuestion(question);
		place.setAnswer(correctAnswer);
		place.setAnswer2(secondAnswer);
		place.setAnswer3(thirdAnswer);

		return true;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		gMap = googleMap;
		if(requireActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) !=
						PackageManager.PERMISSION_GRANTED) {
			requestPermissions(new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
					                   android.Manifest.permission.ACCESS_FINE_LOCATION},
			                   PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION);
			Toast.makeText(requireActivity(), getString(R.string.rejected_location_access),
			               Toast.LENGTH_SHORT).show();
			return;
		}
		setPlace();
	}

	@SuppressLint("MissingPermission")
	private void setPlace() {
		Location usrLocation = getLastKnownLocation();
		if(gMap != null) {
			gMap.setMyLocationEnabled(true);
			gMap.setOnMapLongClickListener(this);
			gMap.getUiSettings().setMyLocationButtonEnabled(true);
			gMap.getUiSettings().setMapToolbarEnabled(false);
			gMap.getUiSettings().setTiltGesturesEnabled(false);

			LatLng usrLatLng = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());
			gMap.clear();
			if(marker != null) {
				gMap.moveCamera(CameraUpdateFactory
						                .newLatLngZoom(marker.getPosition(), CAMERA_UPDATE_ZOOM));
				gMap.addMarker(marker);
			} else {
				gMap.animateCamera(
						CameraUpdateFactory.newLatLngZoom(usrLatLng, CAMERA_UPDATE_ZOOM));
			}
		}
	}

	@SuppressLint("MissingPermission")
	private Location getLastKnownLocation() {
		LocationManager locationManager = (LocationManager) requireActivity()
				.getSystemService(LOCATION_SERVICE);
		List<String> providers = locationManager.getProviders(true);
		Location bestLocation = null;
		for(String provider : providers) {
			Location l = locationManager.getLastKnownLocation(provider);
			if(l == null) {
				continue;
			}
			if(bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
				// Found best last known location: %s", l);
				bestLocation = l;
			}
		}
		return bestLocation;
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
			@NonNull int[] grantResults) {
		if(requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION) {
			if(permissions[0].equals(Manifest.permission.ACCESS_COARSE_LOCATION) &&
					grantResults[0] == PackageManager.PERMISSION_GRANTED &&
					permissions[1].equals(Manifest.permission.ACCESS_FINE_LOCATION) &&
					grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				setPlace();
			}
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_place, container, false);
		ButterKnife.bind(this, view);

		new_place_map_view.onCreate(savedInstanceState);
		new_place_map_view.onResume();
		new_place_map_view.getMapAsync(this);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		question_switch.setOnCheckedChangeListener((buttonView, isChecked) -> question_layout
				.setVisibility(isChecked ?
						               View.VISIBLE :
						               View.GONE));
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		Bundle b = getArguments();
		if(b != null) {
			int position = b.getInt(PLACE_POSITION, viewModel.getPlaces().size());
			place = viewModel.getPlace(position);
			if(place != null) {
				if(place.getName() != null) {
					Objects.requireNonNull(new_place_name.getEditText()).setText(place.getName());
				}
				if(place.getDescription() != null) {
					Objects.requireNonNull(new_place_description.getEditText())
							.setText(place.getDescription());
				}
				if(place.getPosition() != null) {
					onMapLongClick(place.getPosition());
				}
			} else {
				Toast.makeText(requireContext(), getResources().getString(R.string.invalid_place),
				               Toast.LENGTH_SHORT).show();
			}
		}
		create_button.setOnClickListener(this);
		if(marker != null) create_button.setText(R.string.update_place);
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		alert_no_place_text.setVisibility(View.GONE);
		MarkerOptions m = new MarkerOptions().position(latLng);
		if(gMap != null) {
			gMap.clear();
			gMap.addMarker(m);
		}
		marker = m;

	}
}
