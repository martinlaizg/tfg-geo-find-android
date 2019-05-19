package com.martinlaizg.geofind.views.fragment.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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

import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginFragment
		extends Fragment
		implements View.OnClickListener {

	private static final String TAG = LoginFragment.class.getSimpleName();
	private static final int RC_SIGN_IN = 0;

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

	@BindView(R.id.google_sign_in_button)
	SignInButton google_sign_in_button;

	private LoginViewModel viewModel;
	private GoogleSignInClient mGoogleSignInClient;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(requestCode == RC_SIGN_IN) {
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handleSignInResult(task);
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void handleSignInResult(Task<GoogleSignInAccount> task) {
		try {
			GoogleSignInAccount account = task.getResult(ApiException.class);

			// Signed in successfully, show authenticated UI.
			if(account != null) {
				viewModel
						.setLogin(account.getEmail(), account.getIdToken(), Login.LoginType.GOOGLE);
			}
			loginWithGoogle(account);
		} catch(ApiException e) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
			loginWithGoogle(null);
		}
	}

	private void loginWithGoogle(GoogleSignInAccount account) {
		if(account != null) {
			String email = account.getEmail();
			String idToken = account.getIdToken();
			viewModel.setLogin(email, idToken, Login.LoginType.GOOGLE);
			viewModel.login().observe(requireActivity(), user -> {
				if(user == null) {
					Log.e(TAG, "loginWithGoogle: Error login", viewModel.getError());
					return;
				}
				Log.i(TAG, "loginWithGoogle: email=" + user.getEmail() + ", username=" +
						user.getUsername());
			});
		}
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		ButterKnife.bind(this, view);

		google_sign_in_button.setSize(SignInButton.SIZE_STANDARD);
		google_sign_in_button.setOnClickListener((v) -> googleSignIn());
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
				GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getResources().getString(R.string.client_id)).requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
		return view;
	}

	private void googleSignIn() {
		Intent signInIntent = mGoogleSignInClient.getSignInIntent();
		startActivityForResult(signInIntent, RC_SIGN_IN);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
		login_button.setOnClickListener(this);
		registry_button
				.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toRegistry));
	}

	@Override
	public void onStart() {
		GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
		if(account != null) {
			loginWithGoogle(account);
		}
		super.onStart();
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
		viewModel.setLogin(email, password, Login.LoginType.OWN);
		viewModel.login().observe(this, user -> {
			if(user == null) {
				email_input.setError(getString(R.string.wrong_email_password));
				password_input.setError(getString(R.string.wrong_email_password));
				return;
			}
			Credential credential = new Credential.Builder(email).setPassword(password).build();
			Preferences.setLoggedUser(PreferenceManager.getDefaultSharedPreferences(
					Objects.requireNonNull(getContext())), user);
			login_button.setEnabled(true);
			loading_spinner.setVisibility(View.GONE);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
		});
	}
}
