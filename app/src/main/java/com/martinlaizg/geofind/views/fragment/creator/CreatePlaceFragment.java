package com.martinlaizg.geofind.views.fragment.creator;

import android.app.AlertDialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreatePlaceFragment
		extends Fragment
		implements View.OnClickListener, OnMapReadyCallback {

	public static final String PLACE_POSITION = "PLACE_POSITION";

	private static final String TAG = CreatePlaceFragment.class.getSimpleName();

	private static final int CAMERA_UPDATE_ZOOM = 15;

	@BindView(R.id.new_place_name_layout)
	TextInputLayout new_place_name;
	@BindView(R.id.new_place_description_layout)
	TextInputLayout new_place_description;
	@BindView(R.id.new_place_create_button)
	MaterialButton create_button;
	@BindView(R.id.new_place_map_view)
	MapView new_place_map_view;
	@BindView(R.id.new_place_address)
	TextView new_place_address;
	@BindView(R.id.new_place_location_button)
	MaterialButton new_place_location_button;

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

	private CreatorViewModel viewModel;
	private GoogleMap googleMap;
	private String image_url;

	@Override
	public void onClick(View v) {
		// Hide the keyboard
		InputMethodManager editTextInput = (InputMethodManager) requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = requireActivity().getCurrentFocus();
		if(currentFocus != null) {
			editTextInput.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}

		if(!storePlace()) return;

		viewModel.savePlace();
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
	}

	/**
	 * Sotre the place data (name, description, image, question) into the viewModel {@link Place}
	 *
	 * @return true if no has errors else false
	 */
	private boolean storePlace() {
		// Get the name
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

		// Get the description
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

		// Get the question
		boolean question = storeQuestion();
		if(!question) return false;

		viewModel.getPlace().setName(placeName);
		viewModel.getPlace().setDescription(placeDescription);

		return true;
	}

	/**
	 * Store the question data into the viewModel {@link Place}
	 *
	 * @return true if no has errors else false
	 */
	private boolean storeQuestion() {
		if(!question_switch.isChecked()) {
			viewModel.getPlace().setQuestion(null);
			viewModel.getPlace().setAnswer(null);
			viewModel.getPlace().setAnswer2(null);
			viewModel.getPlace().setAnswer3(null);
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
		viewModel.getPlace()
				.setCompleteQuestion(question, correctAnswer, secondAnswer, thirdAnswer);

		return true;
	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		this.googleMap = googleMap;
		this.googleMap.getUiSettings().setAllGesturesEnabled(false);
		this.googleMap.clear();
		setPosition(viewModel.getPlace());
	}

	private void setPosition(Place place) {
		if(place == null) return;
		LatLng position = place.getPosition();
		if(position == null) return;

		// Change button text
		new_place_location_button.setText(getString(R.string.update));
		new_place_address.setVisibility(View.VISIBLE);

		// Get address
		Geocoder gc = new Geocoder(requireContext());
		List<Address> locations;
		new_place_address
				.setText(getString(R.string.two_csv, position.latitude, position.longitude));
		try {
			locations = gc.getFromLocation(position.latitude, position.longitude, 1);
			if(locations != null && locations.size() > 1) {
				new_place_address.setText(locations.get(0).getAddressLine(0));
			}
		} catch(IOException e) {
			e.printStackTrace();
		}

		if(googleMap != null) {
			// Set static map
			MarkerOptions marker = new MarkerOptions().position(position);
			googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(position, CAMERA_UPDATE_ZOOM));
			googleMap.addMarker(marker);
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_create_place, container, false);
		ButterKnife.bind(this, view);
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
		// Show/hide question layout
		question_switch.setOnCheckedChangeListener((buttonView, isChecked) -> question_layout
				.setVisibility(isChecked ?
						               View.VISIBLE :
						               View.GONE));

		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		Bundle b = getArguments();
		if(b != null) {
			int position = b.getInt(PLACE_POSITION, viewModel.getPlaces().size());
			viewModel.retrievePlace(position);
			setPlace();
		}
		create_button.setOnClickListener(this);
		new_place_image_button.setOnClickListener(v -> {
			AlertDialog alertDialog = buildDialog();
			alertDialog.show();
		});

		new_place_location_button.setOnClickListener(v -> {
			if(!storePlace()) return;

			// Hide the keyboard
			InputMethodManager editTextInput = (InputMethodManager) requireActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			View currentFocus = requireActivity().getCurrentFocus();
			if(currentFocus != null) {
				editTextInput.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
			}

			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toCreatePlaceLocation);
		});
	}

	private void setPlace() {
		Place place = viewModel.getPlace();
		if(place == null) {
			Toast.makeText(requireContext(), getString(R.string.something_went_wrong),
			               Toast.LENGTH_SHORT).show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
			return;
		}

		// Set name
		if(place.getName() != null) {
			Objects.requireNonNull(new_place_name.getEditText()).setText(place.getName());
		}

		// Set description
		if(place.getDescription() != null) {
			Objects.requireNonNull(new_place_description.getEditText())
					.setText(place.getDescription());
		}
		// Set location marker
		setPosition(place);

		// Set question
		question_switch.setChecked(false);
		if(place.getQuestion() != null) {
			question_switch.setChecked(true);
			Objects.requireNonNull(new_place_question.getEditText()).setText(place.getQuestion());
			Objects.requireNonNull(new_place_correct_answer.getEditText())
					.setText(place.getAnswer());
			Objects.requireNonNull(new_place_answer_2.getEditText()).setText(place.getAnswer2());
			Objects.requireNonNull(new_place_answer_3.getEditText()).setText(place.getAnswer3());
		}

		// Set image
		place_image_view.setVisibility(View.GONE);
		if(place.getImage() != null && !place.getImage().isEmpty()) {
			Picasso.with(requireContext()).load(image_url).into(place_image_view);
			place_image_view.setVisibility(View.VISIBLE);
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

	private void showExitDialog() {
		new MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
				.setMessage(getString(R.string.exit_lose_data_alert))
				.setPositiveButton(getString(R.string.ok), (dialog, which) -> {
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack();
				}).show();
	}
}
