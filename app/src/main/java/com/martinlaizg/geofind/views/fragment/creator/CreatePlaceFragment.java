package com.martinlaizg.geofind.views.fragment.creator;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
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
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;
import com.squareup.picasso.Picasso;

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

	@BindView(R.id.new_place_image_button)
	MaterialButton new_place_image_button;
	@BindView(R.id.place_image_view)
	ImageView place_image_view;
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
	private String image_url;

	private CreatorViewModel viewModel;
	private MarkerOptions marker;
	private GoogleMap gMap;
	private Place place;

	@Override
	public void onClick(View v) {
		// Hide the keyboard
		InputMethodManager editTextInput = (InputMethodManager) requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = requireActivity().getCurrentFocus();
		if(currentFocus != null) {
			editTextInput.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}

		alert_no_place_text.setVisibility(View.INVISIBLE);
		Place place = getPlace();
		if(place == null) return;

		viewModel.setPlace(place);
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
	}

	/**
	 * Get the place data from de inputs
	 *
	 * @return the place
	 */
	private Place getPlace() {
		// Get the name
		String placeName = Objects.requireNonNull(new_place_name.getEditText()).getText().toString()
				.trim();
		if(placeName.isEmpty()) {
			new_place_name.setError(getString(R.string.required_name));
			return null;
		}
		if(placeName.length() > getResources().getInteger(R.integer.max_name_length)) {
			new_place_name.setError(getString(R.string.text_too_long));
			return null;
		}
		new_place_name.setError("");

		// Get the description
		String placeDescription = Objects.requireNonNull(new_place_description.getEditText())
				.getText().toString().trim();
		if(placeDescription.isEmpty()) {
			new_place_description.setError(getString(R.string.required_description));
			return null;
		}
		if(placeDescription.length() >
				getResources().getInteger(R.integer.max_description_length)) {
			new_place_description.setError(getString(R.string.text_too_long));
			return null;
		}
		new_place_description.setError("");

		// Get the marker
		if(marker == null) {
			alert_no_place_text.setVisibility(View.VISIBLE);
			return null;
		}

		// Get the question
		boolean question = getQuestion();
		if(!question) return null;

		place.setName(placeName);
		place.setDescription(placeDescription);
		place.setPosition(marker.getPosition());

		return place;
	}

	private boolean getQuestion() {
		if(!question_switch.isChecked()) {
			place.setQuestion(null);
			place.setAnswer(null);
			place.setAnswer2(null);
			place.setAnswer3(null);
			return true;
		}

		// Check the question
		String question = Objects.requireNonNull(new_place_question.getEditText()).getText()
				.toString().trim();
		new_place_question.setError("");
		if(question.isEmpty()) {
			new_place_question.setError(getString(R.string.required_question));
			return false;
		}

		// Check the correct answer
		String correctAnswer = Objects.requireNonNull(new_place_correct_answer.getEditText())
				.getText().toString().trim();
		new_place_correct_answer.setError("");
		if(correctAnswer.isEmpty()) {
			new_place_correct_answer.setError(getString(R.string.required_answer));
			return false;
		}

		// Check the second answer
		String secondAnswer = Objects.requireNonNull(new_place_answer_2.getEditText()).getText()
				.toString().trim();
		new_place_answer_2.setError("");
		if(secondAnswer.isEmpty()) {
			new_place_answer_2.setError(getString(R.string.required_answer));
			return false;
		}

		// Check the third answer
		String thirdAnswer = Objects.requireNonNull(new_place_answer_3.getEditText()).getText()
				.toString().trim();
		new_place_answer_3.setError("");
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
		setMarker();
	}

	@SuppressLint("MissingPermission")
	private void setMarker() {
		Location usrLocation = getLastKnownLocation();
		if(gMap != null) {
			gMap.setMyLocationEnabled(true);
			gMap.setOnMapLongClickListener(this);
			gMap.getUiSettings().setMyLocationButtonEnabled(true);
			gMap.getUiSettings().setMapToolbarEnabled(false);
			gMap.getUiSettings().setTiltGesturesEnabled(false);
			gMap.getUiSettings().setZoomControlsEnabled(true);

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
				setMarker();
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

		// Back button listener
		requireActivity().getOnBackPressedDispatcher()
				.addCallback(this, new OnBackPressedCallback(true) {
					@Override
					public void handleOnBackPressed() {
						showExitDialog();
					}
				});
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
			setPlace(viewModel.getPlace(position));
			Log.i(TAG, "onViewCreated: Get place and set");
		}
		create_button.setOnClickListener(this);
		new_place_image_button.setOnClickListener(v -> {
			AlertDialog alertDialog = buildDialog();
			alertDialog.show();
		});
		if(marker != null) create_button.setText(R.string.update_place);
	}

	private void setPlace(Place place) {
		this.place = place;
		if(place != null) {
			// Set name
			if(place.getName() != null) {
				Objects.requireNonNull(new_place_name.getEditText()).setText(place.getName());
			}
			// Set description
			if(place.getDescription() != null) {
				Objects.requireNonNull(new_place_description.getEditText())
						.setText(place.getDescription());
			}
			// Set position (marker)
			if(place.getPosition() != null) {
				onMapLongClick(place.getPosition());
			}
			// Set question
			question_switch.setChecked(false);
			if(place.getQuestion() != null) {
				question_switch.setChecked(true);
				Objects.requireNonNull(new_place_question.getEditText())
						.setText(place.getQuestion());
				Objects.requireNonNull(new_place_correct_answer.getEditText())
						.setText(place.getAnswer());
				Objects.requireNonNull(new_place_answer_2.getEditText())
						.setText(place.getAnswer2());
				Objects.requireNonNull(new_place_answer_3.getEditText())
						.setText(place.getAnswer3());
			}

			place_image_view.setVisibility(View.GONE);

			if(place.getImage() != null && !place.getImage().isEmpty()) {
				Picasso.with(requireContext()).load(image_url).into(place_image_view);
				place_image_view.setVisibility(View.VISIBLE);
			}
		} else {
			Toast.makeText(requireContext(), getResources().getString(R.string.invalid_place),
			               Toast.LENGTH_SHORT).show();
		}
	}

	private AlertDialog buildDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		// Get the layout inflater
		LayoutInflater inflater = requireActivity().getLayoutInflater();

		View view = inflater.inflate(R.layout.add_image_layout, new LinearLayout(requireContext()));
		TextInputLayout image_url_layout = view.findViewById(R.id.image_url_layout);
		Objects.requireNonNull(image_url_layout.getEditText()).setText(image_url);
		return builder.setView(view).setPositiveButton(R.string.ok, (dialog, id) -> {
			image_url = Objects.requireNonNull(image_url_layout.getEditText()).getText().toString();
			place_image_view.setVisibility(View.GONE);
			if(!image_url.isEmpty()) {
				Picasso.with(requireContext()).load(image_url).into(place_image_view);
				place_image_view.setVisibility(View.VISIBLE);
			}

			// Hide the keyboard
			InputMethodManager editTextInput = (InputMethodManager) requireActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			View currentFocus = requireActivity().getCurrentFocus();
			if(currentFocus != null) {
				editTextInput.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
			}
		}).create();
	}

	@Override
	public void onMapLongClick(LatLng latLng) {
		alert_no_place_text.setVisibility(View.INVISIBLE);
		MarkerOptions m = new MarkerOptions().position(latLng);
		if(gMap != null) {
			gMap.clear();
			gMap.addMarker(m);
		}
		marker = m;

	}

	private void showExitDialog() {
		new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
				.setMessage(getString(R.string.exit_lose_data_alert))
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack();
				}).show();
	}
}
