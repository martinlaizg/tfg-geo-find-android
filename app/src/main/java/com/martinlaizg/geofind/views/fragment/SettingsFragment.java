package com.martinlaizg.geofind.views.fragment;


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
		findPreference(getString(R.string.preferences_log_out)).setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
			@Override
			public boolean onPreferenceClick(Preference preference) {
				SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
				Preferences.logout(sp);
				Navigation.findNavController(getActivity(), R.id.main_fragment_holder).popBackStack(R.id.main, false);
				return true;
			}
		});
	}
}
