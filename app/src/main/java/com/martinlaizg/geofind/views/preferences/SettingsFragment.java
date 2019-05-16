package com.martinlaizg.geofind.views.preferences;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;

import java.util.Objects;

public class SettingsFragment
		extends PreferenceFragmentCompat {

	private static final String TAG = SettingsFragment.class.getSimpleName();
	private AlertDialog dialog;
	private UserRepository userRepo;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		userRepo = RepositoryFactory.getUserRepository(requireActivity().getApplication());

	}

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.app_preferences);
		setLogoutPreference();

	}

	private void setLogoutPreference() {
		findPreference(getString(R.string.log_out)).setOnPreferenceClickListener(preference -> {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
			Preferences.logout(sp);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack();
			return true;
		});
		AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
		View view = getLayoutInflater().inflate(R.layout.support_message, null);
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

			new Thread(() -> {
				try {
					userRepo.sendMessage(title, message);
					Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show();
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack();
				} catch(APIException e) {
					Log.e(TAG, "setLogoutPreference: ", e);
					Toast.makeText(requireContext(), "", Toast.LENGTH_SHORT).show();
				}
			}).start();
		});
		builder.setView(view);
		dialog = builder.create();
		findPreference("support").setOnPreferenceClickListener(preference -> {
			dialog.show();
			return true;
		});
	}
}
