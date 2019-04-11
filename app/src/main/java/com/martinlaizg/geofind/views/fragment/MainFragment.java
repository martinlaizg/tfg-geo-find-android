package com.martinlaizg.geofind.views.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.views.activity.MainActivity;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

public class MainFragment
		extends Fragment {


	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (!isLogged()) {
			return null;
		}
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	private boolean isLogged() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(requireActivity());
		User u = Preferences.getLoggedUser(sp);
		MainActivity mainActivity = (MainActivity) requireActivity();
		if (u != null && !u.getEmail().isEmpty()) {
			mainActivity.setDrawerHeader(u.getUsername(), u.getName());
			mainActivity.disableToolbarAndDrawer(true);
			return true;
		}
		// If user is not logged, go to LoginFragment
		mainActivity.disableToolbarAndDrawer(false);
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toLogin);
		return false;
	}

}
