package com.martinlaizg.geofind.views.fragment.login;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.Crypto;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegistryFragment
		extends Fragment {

	private static final String TAG = RegistryFragment.class.getSimpleName();

	@BindView(R.id.email_input)
	TextInputLayout email_input;
	@BindView(R.id.password_input)
	TextInputLayout password_input;
	@BindView(R.id.c_password_input)
	TextInputLayout c_password_input;
	@BindView(R.id.btn_registry)
	MaterialButton btn_registry;

	private LoginViewModel viewModel;

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_registry, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel.class);
		btn_registry.setOnClickListener(v -> registry());
	}

	private void registry() {

		final String email = Objects.requireNonNull(email_input.getEditText()).getText().toString()
				.trim();
		final String password = Objects.requireNonNull(password_input.getEditText()).getText()
				.toString().trim();
		final String c_password = Objects.requireNonNull(c_password_input.getEditText()).getText()
				.toString().trim();

		btn_registry.setEnabled(false);
		if(Objects.requireNonNull(email_input.getEditText()).getText().toString().trim()
				.isEmpty()) {
			email_input.setError(getString(R.string.required_password));
			return;
		}
		if(password.isEmpty()) {
			password_input.setError(getString(R.string.required_verify_password));
			return;
		}
		if(c_password.isEmpty()) {
			c_password_input.setError(getString(R.string.required_verify_password));
			return;
		}
		if(!c_password.equals(password)) {
			c_password_input.setError(getString(R.string.password_does_not_match));
			return;
		}

		Login l = new Login(email, Crypto.hash(password));
		viewModel.registry(l).observe(this, (user) -> {
			btn_registry.setEnabled(true);
			if(user == null) {
				if(viewModel.getError().getType() == ErrorType.EMAIL) {
					email_input.setError(getString(R.string.email_in_use));
				} else {
					Toast.makeText(requireContext(), getString(R.string.other_error),
					               Toast.LENGTH_SHORT).show();
				}
				return;
			}
			Toast.makeText(requireActivity(), getResources().getString(R.string.registry),
			               Toast.LENGTH_LONG).show();
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();

		});
	}
}
