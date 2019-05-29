package com.martinlaizg.geofind;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.EditProfileViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditProfileFragment
		extends Fragment {

	@BindView(R.id.name_input)
	TextInputLayout name_input;
	@BindView(R.id.username_input)
	TextInputLayout username_input;
	@BindView(R.id.email_input)
	TextInputLayout email_input;

	@BindView(R.id.change_image_button)
	MaterialButton change_image_button;
	@BindView(R.id.save_button)
	MaterialButton save_button;

	@BindView(R.id.password_input)
	TextInputLayout password_input;
	@BindView(R.id.change_password_button)
	MaterialButton change_password_button;

	private TextInputLayout confirm_password_input;

	private SharedPreferences sp;
	private User user;
	private EditProfileViewModel viewModel;
	private AlertDialog.Builder password_dialog_builder;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
		user = Preferences.getLoggedUser(sp);
		viewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);

		// Password confirm dialog
		password_dialog_builder = new AlertDialog.Builder(requireContext())
				.setTitle(R.string.confirm_password);
		View dialogView = getLayoutInflater()
				.inflate(R.layout.password_field, new LinearLayout(requireContext()), false);
		confirm_password_input = dialogView.findViewById(R.id.confirm_password);
		password_dialog_builder.setView(dialogView);

		// Buttons
		save_button.setOnClickListener(v -> {
			password_dialog_builder.setPositiveButton(R.string.save, (dialog, which) -> saveUser());
			password_dialog_builder.create().show();
		});
		change_password_button.setOnClickListener(v -> {
			password_dialog_builder
					.setPositiveButton(R.string.save, (dialog, which) -> changeUserPassword());
			password_dialog_builder.create().show();
		});
	}

	private void saveUser() {
		String name = Objects.requireNonNull(name_input.getEditText()).getText().toString().trim();
		String username = Objects.requireNonNull(username_input.getEditText()).getText().toString()
				.trim();
		String email = Objects.requireNonNull(email_input.getEditText()).getText().toString()
				.trim();
		String password = Objects.requireNonNull(confirm_password_input.getEditText()).getText()
				.toString().trim();

		user = Preferences.getLoggedUser(sp);
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);

		updateUser(user, false);
	}

	private void changeUserPassword() {
		password_input.setError("");
		String password = Objects.requireNonNull(password_input.getEditText()).getText().toString()
				.trim();
		String c_password = Objects.requireNonNull(confirm_password_input.getEditText()).getText()
				.toString().trim();

		if(!password.equals(c_password)) {
			password_input.setError(getString(R.string.password_does_not_match));
			confirm_password_input.getEditText().setText("");
			return;
		}

		user = Preferences.getLoggedUser(sp);
		user.setPassword(password);

		updateUser(user, true);
	}

	private void updateUser(User user, boolean isChangePassword) {
		viewModel.updateUser(user, isChangePassword).observe(requireActivity(), u -> {
			if(u == null) {
				ErrorType error = viewModel.getError().getType();
				Toast.makeText(requireContext(), "Algo ha ido mal " + error.toString(),
				               Toast.LENGTH_SHORT).show();
				return;
			}
			Preferences.setLoggedUser(sp, u);
			Toast.makeText(requireContext(), getString(R.string.user_saved), Toast.LENGTH_SHORT)
					.show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
		});
	}
}
