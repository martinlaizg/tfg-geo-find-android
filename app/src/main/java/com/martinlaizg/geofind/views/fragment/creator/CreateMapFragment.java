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
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.views.viewmodel.MapCreatorViewModel;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateMapFragment
		extends Fragment {

	public static final String MAP_ID = "MAP_ID";

	@BindView(R.id.new_map_name_layout)
	TextInputLayout new_map_name;
	@BindView(R.id.new_map_description_layout)
	TextInputLayout new_map_description;

	@BindView(R.id.done_button)
	MaterialButton doneButton;
	@BindView(R.id.add_image_button)
	MaterialButton add_image_button;
	@BindView(R.id.difficulty_spinner)
	Spinner difficultySpinner;

	private MapCreatorViewModel viewModel;
	private NavController navController;


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		final View view = inflater.inflate(R.layout.fragment_create_map, container, false);
		ButterKnife.bind(this, view);
		viewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);
		setInputs(viewModel.getCreatedMap());
		Bundle b = getArguments();
		if (b != null) {
			String map_id = b.getString(MAP_ID);
			if (map_id != null && !map_id.isEmpty()) {
				viewModel.getMap(map_id).observe(getActivity(), map -> {
					viewModel.setMap(map);
					setInputs(map);
				});
			}
		}
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		doneButton.setOnClickListener(v -> {
			try {
				if (Objects.requireNonNull(new_map_name.getEditText()).getText().toString().trim().isEmpty()) {
					new_map_name.setError(getString(R.string.required_name));
					return;
				}
				if (new_map_name.getEditText().getText().toString().trim().length() > getResources().getInteger(R.integer.max_name_length)) {
					new_map_name.setError(getString(R.string.you_oversized));
					return;
				}
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

			v.clearFocus();

			String name = new_map_name.getEditText().getText().toString().trim();
			String description = new_map_description.getEditText().getText().toString().trim();
			PlayLevel pl = PlayLevel.getPlayLevel(difficultySpinner.getSelectedItemPosition());

			User user = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(getContext()));
			viewModel.setMap(name, description, user.getId(), pl);
			Navigation.findNavController(getActivity(), R.id.main_fragment_holder).navigate(R.id.toCreator);
		});
	}

	private void setInputs(Map m) {
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
	}
}
