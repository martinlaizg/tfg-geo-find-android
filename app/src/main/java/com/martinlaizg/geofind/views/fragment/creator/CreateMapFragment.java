package com.martinlaizg.geofind.views.fragment.creator;


import android.os.Bundle;
import android.text.TextUtils;
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

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateMapFragment
		extends Fragment {

	@BindView(R.id.new_map_name_layout)
	TextInputLayout new_map_name;
	@BindView(R.id.new_map_description_layout)
	TextInputLayout new_map_description;

	@BindView(R.id.done_button)
	MaterialButton doneButton;
	@BindView(R.id.add_image_button)
	MaterialButton add_image_button;
	@BindView(R.id.dificulty_spinner)
	Spinner dificultySpinner;

	MapCreatorViewModel viewModel;
	private NavController navController;

	public CreateMapFragment() {
		// Required empty public constructor
	}


	@Override
	public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		final View view = inflater.inflate(R.layout.fragment_create_map, container, false);
		ButterKnife.bind(this, view);
		viewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);

		doneButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					if (TextUtils.isEmpty(new_map_name.getEditText().getText())) {
						new_map_name.setError(getString(R.string.required_name));
						return;
					}
					if (TextUtils.isEmpty(new_map_description.getEditText().getText())) {
						new_map_description.setError(getString(R.string.required_description));
						return;
					}
				} catch (NullPointerException ex) {
					Toast.makeText(getContext(), "View incorrecto", Toast.LENGTH_SHORT).show();
					return;
				}

				String name = new_map_name.getEditText().getText().toString().trim();
				String description = new_map_description.getEditText().getText().toString().trim();
				PlayLevel pl = PlayLevel.getPlayLevel(dificultySpinner.getSelectedItemPosition());

				User user = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(getContext()));
				viewModel.setMap(name, description, user.getId(), pl);
				Navigation.findNavController(getActivity(), R.id.main_fragment_holder).popBackStack();
			}
		});

		setInputs();

		return view;
	}

	private void setInputs() {
		Map m = viewModel.getCreatedMap();
		if (!m.getName().isEmpty()) {
			Objects.requireNonNull(new_map_name.getEditText()).setText(m.getName());
		}
		if (!m.getDescription().isEmpty()) {
			Objects.requireNonNull(new_map_description.getEditText()).setText(m.getDescription());
		}
		if (m.getMin_level() != null) {
			dificultySpinner.setSelection(m.getMin_level().ordinal());
		}
	}
}
