package com.martinlaizg.geofind.views.fragment;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.activity.MainActivity;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.preference.PreferenceManager;

public class MainFragment
		extends Fragment {


	public MainFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if (!isLogged()) {
			return null;
		}
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

	private boolean isLogged() {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
		User u = Preferences.getLoggedUser(sp);
		MainActivity mainActivity = (MainActivity) getActivity();
		if (u != null && !u.getEmail().isEmpty()) {
			mainActivity.setDrawerHeader(u.getUsername(), u.getName());
			mainActivity.setToolbarVisibility(true);
			return true;
		}
		// If user is not logged, go to LoginFragment
		mainActivity.setToolbarVisibility(false);
		Navigation.findNavController(getActivity(), R.id.main_fragment_holder).navigate(R.id.toLogin);
		return false;
	}

}
