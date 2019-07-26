package com.martinlaizg.geofind.views.fragment.creator;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTourFragment
		extends Fragment
		implements View.OnClickListener {

	private final static String TAG = CreateTourFragment.class.getSimpleName();

	@BindView(R.id.tour_name_layout)
	TextInputLayout tour_name_layout;
	@BindView(R.id.tour_description_layout)
	TextInputLayout tour_description_layout;
	@BindView(R.id.tour_image_view)
	ImageView tour_image_view;

	@BindView(R.id.done_button)
	MaterialButton done_button;
	@BindView(R.id.add_image_button)
	MaterialButton add_image_button;
	@BindView(R.id.difficulty_spinner)
	Spinner difficulty_spinner;

	private CreatorViewModel viewModel;
	private String image_url = "";

	@Override
	public View onCreateView(@NonNull final LayoutInflater inflater, final ViewGroup container,
			final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_create_tour, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		Tour t = viewModel.getTour();
		if(t != null) {
			if(!t.getName().isEmpty()) {
				Objects.requireNonNull(tour_name_layout.getEditText()).setText(t.getName());
			}
			if(!t.getDescription().isEmpty()) {
				Objects.requireNonNull(tour_description_layout.getEditText())
						.setText(t.getDescription());
			}
			if(t.getMin_level() != null) {
				difficulty_spinner.setSelection(t.getMin_level().ordinal());
			}
			tour_image_view.setVisibility(View.GONE);
			if(t.getImage() != null) image_url = t.getImage();
			if(!image_url.isEmpty()) {
				Picasso.with(requireContext()).load(image_url).into(tour_image_view);
				tour_image_view.setVisibility(View.VISIBLE);
			}
		}
		done_button.setOnClickListener(this);
		add_image_button.setOnClickListener(v -> {
			AlertDialog alertDialog = buildDialog();
			alertDialog.show();
		});
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
			tour_image_view.setVisibility(View.GONE);
			if(!image_url.isEmpty()) {
				Picasso.with(requireContext()).load(image_url).into(tour_image_view);
				tour_image_view.setVisibility(View.VISIBLE);
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
	public void onClick(View v) {
		tour_name_layout.setError("");
		String name = Objects.requireNonNull(tour_name_layout.getEditText()).getText().toString()
				.trim();
		if(name.isEmpty()) {
			tour_name_layout.setError(getString(R.string.required_name));
			return;
		}
		tour_name_layout.setError("");
		if(name.length() > getResources().getInteger(R.integer.max_name_length)) {
			tour_name_layout.setError(getString(R.string.text_too_long));
			return;
		}
		tour_description_layout.setError("");
		String description = Objects.requireNonNull(tour_description_layout.getEditText()).getText()
				.toString().trim();
		if(description.isEmpty()) {
			tour_description_layout.setError(getString(R.string.required_description));
			return;
		}
		if(description.length() > getResources().getInteger(R.integer.max_description_length)) {
			tour_description_layout.setError(getString(R.string.text_too_long));
			return;
		}

		PlayLevel pl = PlayLevel.getPlayLevel(difficulty_spinner.getSelectedItemPosition());
		User user = Preferences
				.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()));
		viewModel.updateTour(name, description, user.getId(), pl, image_url);
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
	}
}
