package com.martinlaizg.geofind.views.fragment.login;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistryFragment
		extends Fragment {

	public static final String ARG_EMAIL = "ARG_EMAIL";
	private static final String TAG = RegistryFragment.class.getSimpleName();

	@BindView(R.id.name_input)
	TextInputLayout name_input;
	@BindView(R.id.email_input)
	TextInputLayout email_input;
	@BindView(R.id.password_input)
	TextInputLayout password_input;
	@BindView(R.id.c_password_input)
	TextInputLayout c_password_input;
	@BindView(R.id.btn_registry)
	MaterialButton btn_registr;

	LoginViewModel viewModel;

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_registry, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity())).get(LoginViewModel.class);
		String email = viewModel.getEmail();
		if (email != null && !email.isEmpty()) {
			email_input.getEditText().setText(email);
		}
		btn_registr.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(final View v) {
				registry();
			}
		});
	}

	private void registry() {
		btn_registr.setVisibility(View.GONE);
		try {
			if (name_input.getEditText().getText().toString().trim().isEmpty()) {
				name_input.setError(getString(R.string.required_name));
				return;
			}
			if (email_input.getEditText().getText().toString().trim().isEmpty()) {
				email_input.setError(getString(R.string.required_password));
				return;
			}
			String password = password_input.getEditText().getText().toString().trim();
			if (password.isEmpty()) {
				password_input.setError(getString(R.string.required_verify_password));
				return;
			}
			final String c_password = c_password_input.getEditText().getText().toString().trim();
			if (c_password.isEmpty()) {
				password_input.setError(getString(R.string.required_verify_password));
				return;
			}
			if (!c_password.equals(password)) {
				password_input.setError(getString(R.string.no_match_password));
				return;
			}
		} catch (NullPointerException ex) {
			Log.e(TAG, "registry: ", ex);
		}

		String name = name_input.getEditText().getText().toString().trim();
		String email = email_input.getEditText().getText().toString().trim();
		String password = password_input.getEditText().getText().toString().trim();
		viewModel.setRegistry(name, email, password);
		viewModel.registry().observe(this, new Observer<User>() {
			@Override
			public void onChanged(User user) {

			}
		});

		Toast.makeText(getActivity(), "Registrado correctamente", Toast.LENGTH_LONG).show();
		// TODO: implement registry
		// TODO: add loading image
	}

}
