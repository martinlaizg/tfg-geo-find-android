package com.martinlaizg.geofind.views.fragment.creator;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.enums.PlayLevel;
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateTourFragment
		extends Fragment
		implements View.OnClickListener {

	@BindView(R.id.new_map_name_layout)
	TextInputLayout new_map_name;
	@BindView(R.id.new_map_description_layout)
	TextInputLayout new_map_description;

	@BindView(R.id.done_button)
	MaterialButton done_button;
	@BindView(R.id.add_image_button)
	MaterialButton add_image_button;
	@BindView(R.id.difficulty_spinner)
	Spinner difficultySpinner;

	private CreatorViewModel viewModel;


	@Override
	public View onCreateView(@NotNull final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_tour_map, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel.class);
		Tour m = viewModel.getTour();
		if (m != null) {
			if (!m.getName().isEmpty()) {
				Objects.requireNonNull(new_map_name.getEditText()).setText(m.getName());
			}
			if (!m.getDescription().isEmpty()) {
				Objects.requireNonNull(new_map_description.getEditText()).setText(m.getDescription());
			}
			if (m.getMin_level() != null) {
				difficultySpinner.setSelection(m.getMin_level().ordinal());
			}
		}
		done_button.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		try {
			new_map_name.setError("");
			if (Objects.requireNonNull(new_map_name.getEditText()).getText().toString().trim().isEmpty()) {
				new_map_name.setError(getString(R.string.required_name));
				return;
			}
			new_map_name.setError("");
			if (new_map_name.getEditText().getText().toString().trim().length() > getResources().getInteger(R.integer.max_name_length)) {
				new_map_name.setError(getString(R.string.you_oversized));
				return;
			}
			new_map_description.setError("");
			if (Objects.requireNonNull(new_map_description.getEditText()).getText().toString().trim().isEmpty()) {
				new_map_description.setError(getString(R.string.required_description));
				return;
			}
			if (new_map_description.getEditText().getText().toString().trim().length() > getResources().getInteger(R.integer.max_description_length)) {
				new_map_description.setError(getString(R.string.you_oversized));
				return;
			}
		} catch (NullPointerException ex) {
			Toast.makeText(getContext(), "View incorrecto", Toast.LENGTH_SHORT).show();
			return;
		}

		String name = new_map_name.getEditText().getText().toString().trim();
		String description = new_map_description.getEditText().getText().toString().trim();
		PlayLevel pl = PlayLevel.getPlayLevel(difficultySpinner.getSelectedItemPosition());

		User user = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()));
		viewModel.setCreatedMap(name, description, user.getId(), pl);
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();

	}
}
