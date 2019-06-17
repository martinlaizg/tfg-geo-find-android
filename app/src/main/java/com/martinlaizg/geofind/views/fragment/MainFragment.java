package com.martinlaizg.geofind.views.fragment;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.activity.MainActivity;

public class MainFragment
		extends Fragment {

	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
		User u = Preferences.getLoggedUser(sp);
		MainActivity mainActivity = (MainActivity) requireActivity();
		if(u == null) {
			// If user is not logged, go to LoginFragment
			mainActivity.setToolbarAndDrawer(false);
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toLogin);
			return null;
		}
		mainActivity.setToolbarAndDrawer(true);
		return inflater.inflate(R.layout.fragment_main, container, false);
	}
}
