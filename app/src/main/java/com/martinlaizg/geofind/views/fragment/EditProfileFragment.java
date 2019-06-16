package com.martinlaizg.geofind.views.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.Crypto;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.viewmodel.EditProfileViewModel;
import com.squareup.picasso.Picasso;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.martinlaizg.geofind.data.access.api.entities.Login.Provider.OWN;

public class EditProfileFragment
		extends Fragment {

	@BindView(R.id.user_image)
	ImageView user_image;
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

	@BindView(R.id.new_password_input)
	TextInputLayout new_password_input;
	@BindView(R.id.c_new_password_input)
	TextInputLayout c_new_password_input;
	@BindView(R.id.change_password_button)
	MaterialButton change_password_button;

	@BindView(R.id.password_layout)
	ConstraintLayout password_layout;

	private TextInputLayout confirm_password_input;

	private SharedPreferences sp;
	private User user;
	private Login login;
	private EditProfileViewModel viewModel;

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
		fillUserData();

		login = Preferences.getLogin(sp);
		if(login.getProvider() != OWN) {
			password_layout.setVisibility(View.GONE);
			email_input.setVisibility(View.GONE);
		}

		viewModel = ViewModelProviders.of(this).get(EditProfileViewModel.class);

		// Buttons
		change_image_button.setOnClickListener(v -> Toast
				.makeText(requireContext(), getString(R.string.work_in_progress),
				          Toast.LENGTH_SHORT).show());
		change_image_button.setVisibility(View.GONE);
		save_button.setOnClickListener(v -> saveAction());
		change_password_button.setOnClickListener(v -> changePasswordAction());
	}

	private void fillUserData() {
		user = Preferences.getLoggedUser(sp);
		login = Preferences.getLogin(sp);
		if(user.getName() != null) {
			Objects.requireNonNull(name_input.getEditText()).setText(user.getName());
		}
		if(user.getUsername() != null) {
			Objects.requireNonNull(username_input.getEditText()).setText(user.getUsername());
		}
		if(user.getEmail() != null) {
			Objects.requireNonNull(email_input.getEditText()).setText(user.getEmail());
		}
		if(user.getImage() != null && !user.getImage().isEmpty()) {
			Picasso.with(requireContext()).load(user.getImage()).into(user_image);
		}
	}

	private void saveAction() {
		String name = Objects.requireNonNull(name_input.getEditText()).getText().toString().trim();
		String username = Objects.requireNonNull(username_input.getEditText()).getText().toString()
				.trim();
		String email = Objects.requireNonNull(email_input.getEditText()).getText().toString()
				.trim();

		user = Preferences.getLoggedUser(sp);
		user.setName(name);
		user.setUsername(username);

		login = Preferences.getLogin(sp);
		if(login.getProvider() == OWN) {
			user.setEmail(email);
			AlertDialog.Builder passADB = getPasswordDialog();
			passADB.setPositiveButton(R.string.save, (dialog, which) -> {
				String password = Objects.requireNonNull(confirm_password_input.getEditText())
						.getText().toString().trim();
				if(login.getSecure().equals(Crypto.hash(password))) {
					updateUser();
				} else {
					Toast.makeText(requireContext(), getString(R.string.wrong_password),
					               Toast.LENGTH_SHORT).show();
				}
				hiddeKeyboard();
				dialog.dismiss();
			});
			passADB.show();
		} else {
			updateUser();
		}
	}

	// Only for Login.Provider.OWN
	private void changePasswordAction() {
		String newPassword = Objects.requireNonNull(new_password_input.getEditText()).getText()
				.toString().trim();
		String cnewPassword = Objects.requireNonNull(c_new_password_input.getEditText()).getText()
				.toString().trim();
		if(newPassword.isEmpty()) {
			new_password_input.setError(getString(R.string.required_password));
			return;
		}

		if(!newPassword.equals(cnewPassword)) {
			c_new_password_input.setError(getString(R.string.password_does_not_match));
			return;
		}

		user = Preferences.getLoggedUser(sp);
		user.setSecure(Crypto.hash(newPassword));

		login = Preferences.getLogin(sp);
		if(login.getProvider() == OWN) {
			AlertDialog.Builder passADB = getPasswordDialog();
			passADB.setPositiveButton(R.string.save, (dialog, which) -> {
				String password = Objects.requireNonNull(confirm_password_input.getEditText())
						.getText().toString().trim();
				if(login.getSecure().equals(Crypto.hash(password))) {
					updateUser();
				} else {
					Toast.makeText(requireContext(), getString(R.string.wrong_password),
					               Toast.LENGTH_SHORT).show();
				}
				hiddeKeyboard();
				dialog.dismiss();
			});
			passADB.show();
		}
	}

	private AlertDialog.Builder getPasswordDialog() {
		AlertDialog.Builder passADB = new AlertDialog.Builder(requireContext())
				.setTitle(R.string.old_password);
		View dialogView = getLayoutInflater()
				.inflate(R.layout.password_field, new LinearLayout(requireContext()), false);
		confirm_password_input = dialogView.findViewById(R.id.confirm_password);
		passADB.setView(dialogView);
		return passADB;
	}

	private void updateUser() {
		save_button.setEnabled(false);
		viewModel.updateUser(login, user).observe(this, newUser -> {
			save_button.setEnabled(true);
			if(newUser == null) {
				ErrorType error = viewModel.getError().getType();
				Toast.makeText(requireContext(), "Algo ha ido mal " + error.toString(),
				               Toast.LENGTH_SHORT).show();
				return;
			}

			login = new Login(newUser.getEmail(), login.getSecure(), login.getProvider());
			if(user.getSecure() != null && !user.getSecure().isEmpty()) {
				login.setSecure(user.getSecure());
				// TODO remove saved credentials
			}
			Preferences.setLogin(sp, login);
			Preferences.setLoggedUser(sp, newUser);

			Toast.makeText(requireContext(), getString(R.string.user_saved), Toast.LENGTH_SHORT)
					.show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
		});
	}

	private void hiddeKeyboard() {
		InputMethodManager inputManager = (InputMethodManager) requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		inputManager.hideSoftInputFromWindow(
				Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
