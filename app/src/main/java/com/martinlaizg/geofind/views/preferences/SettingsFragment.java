package com.martinlaizg.geofind.views.preferences;


import android.content.SharedPreferences;
import android.os.Bundle;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;

import androidx.navigation.Navigation;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;


public class SettingsFragment
		extends PreferenceFragmentCompat {


	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		addPreferencesFromResource(R.xml.app_preferences);
		setLogoutPreference();

	}


	private void setLogoutPreference() {
		findPreference(getString(R.string.log_out)).setOnPreferenceClickListener(preference -> {
			SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireContext());
			Preferences.logout(sp);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack();
			return true;
		});
	}
}
