package com.martinlaizg.geofind.views.fragment.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.credentials.Credentials;
import com.google.android.gms.auth.api.credentials.CredentialsClient;
import com.google.android.gms.auth.api.credentials.CredentialsOptions;
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
import com.martinlaizg.geofind.data.Crypto;
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
	private static final int RC_SAVE = 1;

	@BindView(R.id.email_input)
	TextInputLayout email_input;
	@BindView(R.id.password_input)
	TextInputLayout password_input;
	@BindView(R.id.login_button)
	MaterialButton login_button;
	@BindView(R.id.login_register_button)
	MaterialButton registry_button;

	@BindView(R.id.load_layout)
	ConstraintLayout load_layout;

	@BindView(R.id.google_sign_in_button)
	SignInButton google_sign_in_button;

	private LoginViewModel viewModel;
	private CredentialsClient mCredentialsClient;
	private GoogleSignInClient mGoogleSignInClient;
	private String sub;

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
			if(account != null) {
				String email = account.getEmail();
				String idToken = account.getIdToken();
				sub = account.getId();
				Login l = new Login(email, idToken, Login.Provider.GOOGLE);
				login(l);
			} else {
				Toast.makeText(requireContext(), getString(R.string.wrong_login),
				               Toast.LENGTH_SHORT).show();
			}
		} catch(ApiException e) {

			Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
		}
	}

	private void login(Login login) {
		viewModel.login(login).observe(this, user -> {
			login_button.setEnabled(true);
			registry_button.setEnabled(true);
			load_layout.setVisibility(View.GONE);
			if(user == null) {
				switch(viewModel.getError().getType()) {
					case TOKEN:
					case PROVIDER:
					case PROVIDER_LOGIN:
						Toast.makeText(requireContext(),
						               getString(R.string.service_provider_problem),
						               Toast.LENGTH_SHORT).show();
						break;
					case EMAIL:
						email_input.setError(getString(R.string.wrong_email));
						break;
					case PASSWORD:
						password_input.setError(getString(R.string.wrong_password));
						break;
					case SECURE:
						break;
					default:
						Toast.makeText(requireContext(), getString(R.string.other_error),
						               Toast.LENGTH_SHORT).show();
				}
				return;
			}

			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
			Preferences.setLoggedUser(sp, user);
			if(login.getProvider() != Login.Provider.OWN) login.setSecure(sub);
			Preferences.setLogin(sp, login);

			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
		});
	}

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_login, container, false);
		ButterKnife.bind(this, view);

		google_sign_in_button.setSize(SignInButton.SIZE_STANDARD);
		google_sign_in_button.setOnClickListener((v) -> googleSignIn());

		// Google SignIn Button
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
				GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getResources().getString(R.string.client_id)).requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);

		// Google SmartLock
		CredentialsOptions options = new CredentialsOptions.Builder().forceEnableSaveDialog()
				.build();
		mCredentialsClient = Credentials.getClient(requireActivity(), options);
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
			String email = account.getEmail();
			String idToken = account.getIdToken();
			Login l = new Login(email, idToken, Login.Provider.GOOGLE);
			login(l);
		}
		super.onStart();
	}

	@Override
	public void onClick(View v) {
		InputMethodManager inputManager = (InputMethodManager) requireActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		inputManager.hideSoftInputFromWindow(
				Objects.requireNonNull(requireActivity().getCurrentFocus()).getWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);

		email_input.clearFocus();
		password_input.clearFocus();

		String email = Objects.requireNonNull(email_input.getEditText()).getText().toString()
				.trim();
		String password = Objects.requireNonNull(password_input.getEditText()).getText().toString()
				.trim();
		if(TextUtils.isEmpty(email)) {
			email_input.setError(getString(R.string.required_email));
			return;
		}
		if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			email_input.setError(getString(R.string.email_wrong_format));
			return;
		}
		if(TextUtils.isEmpty(Objects.requireNonNull(password_input.getEditText()).getText())) {
			password_input.setError(getString(R.string.required_password));
			return;
		}

		login_button.setEnabled(false);
		registry_button.setEnabled(false);
		load_layout.setVisibility(View.VISIBLE);

		Login l = new Login(email, Crypto.hash(password));
		login(l);
	}
}
