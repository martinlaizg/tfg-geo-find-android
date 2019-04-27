package com.martinlaizg.geofind.views.fragment.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

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
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment
		extends Fragment
		implements View.OnClickListener {

	private static final String TAG = LoginFragment.class.getSimpleName();

	@BindView(R.id.email_input)
	TextInputLayout email_input;
	@BindView(R.id.password_input)
	TextInputLayout password_input;
	@BindView(R.id.login_button)
	MaterialButton login_button;
	@BindView(R.id.login_register_button)
	MaterialButton registry_button;
	@BindView(R.id.loading_spinner)
	ProgressBar loading_spinner;

	private LoginViewModel viewModel;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
		login_button.setOnClickListener(this);
		registry_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toRegistry));
	}

	@Override
	public void onClick(View v) {
		if(TextUtils.isEmpty(Objects.requireNonNull(email_input.getEditText()).getText())) {
			email_input.setError(getString(R.string.required_email));
			return;
		}
		if(TextUtils.isEmpty(Objects.requireNonNull(password_input.getEditText()).getText())) {
			password_input.setError(getString(R.string.required_password));
			return;
		}
		login_button.setEnabled(false);
		loading_spinner.setVisibility(View.VISIBLE);

		String email = email_input.getEditText().getText().toString().trim();
		String password = password_input.getEditText().getText().toString().trim();
		viewModel.setLogin(email, password);
		viewModel.login().observe(this, user -> {
			if(user == null) {
				email_input.setError(getString(R.string.wrong_email_password));
				password_input.setError(getString(R.string.wrong_email_password));
				return;
			}
			Preferences.setLoggedUser(PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext())), user);
			login_button.setEnabled(true);
			loading_spinner.setVisibility(View.GONE);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
		});
	}
}
