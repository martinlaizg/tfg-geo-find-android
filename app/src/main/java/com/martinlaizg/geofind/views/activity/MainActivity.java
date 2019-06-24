package com.martinlaizg.geofind.views.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.service.PlayService;
import com.martinlaizg.geofind.data.access.api.service.TourService;
import com.martinlaizg.geofind.data.access.api.service.UserService;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.squareup.picasso.Picasso;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
		extends AppCompatActivity
		implements SharedPreferences.OnSharedPreferenceChangeListener {

	private static final String TAG = MainActivity.class.getSimpleName();

	@BindView(R.id.drawer_layout)
	DrawerLayout drawer_layout;
	@BindView(R.id.main_toolbar)
	Toolbar toolbar;

	@BindView(R.id.drawer_nav_view)
	NavigationView navigationView;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		switch(key) {
			case Preferences.USER:
				User u = Preferences.getLoggedUser(sharedPreferences);
				if(u != null) {
					setDrawerHeader(u.getUsername(), u.getName(), u.getImage());
				} else {
					setToolbarAndDrawer(false);
				}
				break;
			case Preferences.TOKEN:
				String token = Preferences.getToken(sharedPreferences);
				TourService.token = token;
				PlayService.token = token;
				UserService.token = token;
				break;
		}
	}

	public void setDrawerHeader(String username, String name, String image) {
		View headerView = navigationView.getHeaderView(0);
		if(name == null) {
			name = getString(R.string.your_account);
		}
		if(username == null) {
			username = "";
		}
		((TextView) headerView.findViewById(R.id.drawer_header_name)).setText(name);
		((TextView) headerView.findViewById(R.id.drawer_header_username)).setText(username);

		if(image != null && !image.isEmpty()) {
			ImageView imageView = headerView.findViewById(R.id.drawer_user_image);
			Picasso.with(getApplicationContext()).load(image).into(imageView);
		}
	}

	public void setToolbarAndDrawer(boolean visibility) {
		toolbar.setVisibility(visibility ?
				                      View.VISIBLE :
				                      View.GONE);
		drawer_layout.setDrawerLockMode(visibility ?
				                                DrawerLayout.LOCK_MODE_UNLOCKED :
				                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

	@Override
	protected void onPause() {
		super.onPause();
		// Unregister the listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		// Set up a listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(this);

		NavController navController = Navigation.findNavController(this, R.id.main_fragment_holder);

		Set<Integer> topLevelDestinations = new HashSet<>();
		topLevelDestinations.add(R.id.navMain);
		topLevelDestinations.add(R.id.navTourList);
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				topLevelDestinations).setDrawerLayout(drawer_layout).build();

		setSupportActionBar(toolbar);
		NavigationUI.setupWithNavController(navigationView, navController);
		NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

		PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
		User u = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(this));
		if(u != null) {
			setDrawerHeader(u.getUsername(), u.getName(), u.getImage());
		}
	}
}
