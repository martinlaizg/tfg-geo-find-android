package com.martinlaizg.geofind.views.preferences;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.views.viewmodel.SettingsViewModel;

import java.util.Objects;

public class SettingsFragment
		extends PreferenceFragmentCompat {

	// TODO refactor
	private static final String TAG = SettingsFragment.class.getSimpleName();
	private AlertDialog dialog;
	private SettingsViewModel viewModel;
	private GoogleSignInClient mGoogleSignInClient;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		viewModel = ViewModelProviders.of(requireActivity()).get(SettingsViewModel.class);
	}

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.app_preferences);

		findPreference(getString(R.string.log_out))
				.setOnPreferenceClickListener(getLogOutListener());
		createSupportMessageDialog();
		findPreference("support").setOnPreferenceClickListener(preference -> {
			dialog.show();
			return true;
		});
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(
				GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getResources().getString(R.string.client_id)).requestEmail()
				.build();
		mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso);
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	private Preference.OnPreferenceClickListener getLogOutListener() {
		return preference -> {
			mGoogleSignInClient.signOut().addOnCompleteListener(requireActivity(), task -> {
				SharedPreferences sp = PreferenceManager
						.getDefaultSharedPreferences(requireContext());
				Preferences.logout(sp);
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack();
			});
			return true;
		};
	}

	private void createSupportMessageDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		View view = getLayoutInflater()
				.inflate(R.layout.support_message, new LinearLayout(requireContext()), false);
		TextInputLayout title_layout = view.findViewById(R.id.title_layout);
		TextInputLayout message_text_layout = view.findViewById(R.id.message_text_layout);
		MaterialButton send_button = view.findViewById(R.id.send_button);
		send_button.setOnClickListener(v -> {
			if(Objects.requireNonNull(title_layout.getEditText()).getText().toString().trim()
					.isEmpty()) {
				title_layout.setError("You should fill it");
				return;
			}
			if(Objects.requireNonNull(message_text_layout.getEditText()).getText().toString().trim()
					.isEmpty()) {
				message_text_layout.setError("You should fill it");
				return;
			}

			String title = title_layout.getEditText().getText().toString();
			String message = message_text_layout.getEditText().getText().toString();

			viewModel.sendMessage(title, message).observe(this, (ok) -> {
				if(ok == null) {
					APIException e = viewModel.getError();
					Log.e(TAG, "setLogoutPreference: " + e.getType().toString());
				} else if(ok) {
					Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show();
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack();
					dialog.dismiss();
				} else {
					Toast.makeText(requireContext(), "Message no sent", Toast.LENGTH_SHORT).show();
				}
			});

		});
		builder.setView(view);
		dialog = builder.create();
	}
}
