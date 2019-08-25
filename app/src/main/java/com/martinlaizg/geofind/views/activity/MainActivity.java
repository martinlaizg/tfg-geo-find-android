package com.martinlaizg.geofind.views.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.preference.PreferenceManager;

import com.google.android.material.navigation.NavigationView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.api.RetrofitInstance;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.UserRepository;
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
	@BindView(R.id.drawer_nav_view)
	NavigationView navigationView;
	@BindView(R.id.toolbar)
	Toolbar toolbar;

	private NavController navController;
	private AppBarConfiguration appBarConfiguration;

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		Log.i(TAG, "onSharedPreferenceChanged: changed");
		switch(key) {
			case Preferences.USER:
				User u = Preferences.getLoggedUser(sharedPreferences);
				if(u != null) {
					setDrawerHeader(u.getUsername(), u.getName(), u.getImage());
				}
				break;
			case Preferences.TOKEN:
				String token = Preferences.getToken(sharedPreferences);
				setServicesToken(token);
				break;
		}
	}

	private void setDrawerHeader(String username, String name, String image) {
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

	private void setServicesToken(String token) {
		RetrofitInstance.setToken(token);
	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState,
			@Nullable PersistableBundle persistentState) {
		super.onCreate(savedInstanceState, persistentState);
		UserRepository.getInstance(getApplication());
	}

	@Override
	public boolean onOptionsItemSelected(@NonNull MenuItem item) {
		return NavigationUI.onNavDestinationSelected(item, navController) ||
				super.onOptionsItemSelected(item);
	}

	@Override
	public void onBackPressed() {
		if(drawer_layout.isDrawerOpen(GravityCompat.START)) {
			drawer_layout.closeDrawer(GravityCompat.START);
		} else {
			super.onBackPressed();
		}
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

		setSupportActionBar(toolbar);

		// Set Navigation Controller
		navController = Navigation.findNavController(this, R.id.main_fragment_holder);

		// Setup the navigation view (drawer)
		NavigationUI.setupWithNavController(navigationView, navController);

		// Set the toolbar
		Set<Integer> topLevelDestinations = new HashSet<>();
		topLevelDestinations.add(R.id.navMain);
		topLevelDestinations.add(R.id.navTourList);
		appBarConfiguration = new AppBarConfiguration.Builder(topLevelDestinations)
				.setDrawerLayout(drawer_layout).build();
		NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

		navController.addOnDestinationChangedListener(this::onDestinationChangedListener);

		PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false);
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		User u = Preferences.getLoggedUser(sp);
		if(u != null) {
			setDrawerHeader(u.getUsername(), u.getName(), u.getImage());
		}
		String token = Preferences.getToken(sp);
		setServicesToken(token);
	}

	private void onDestinationChangedListener(NavController navController,
			NavDestination destination, Bundle bundle) {
		if(destination.getId() == R.id.navLogin || destination.getId() == R.id.navRegistry) {
			toolbar.setVisibility(View.GONE);
			drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
		} else {
			toolbar.setVisibility(View.VISIBLE);
			drawer_layout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
		}
	}

	@Override
	public boolean onSupportNavigateUp() {
		return NavigationUI.navigateUp(navController, appBarConfiguration) ||
				super.onSupportNavigateUp();
	}
}
