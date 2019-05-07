package com.martinlaizg.geofind.views.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.PlaceRepository;
import com.martinlaizg.geofind.data.repository.PlayRepository;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.TourRepository;

import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity
		extends AppCompatActivity {

	private static final String TAG = MainActivity.class.getSimpleName();

	@BindView(R.id.drawer_layout)
	DrawerLayout drawer_layout;
	@BindView(R.id.main_toolbar)
	Toolbar toolbar;

	@BindView(R.id.drawer_nav_view)
	NavigationView navigationView;

	NavController navController;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(MainActivity.this);
		sp = PreferenceManager.getDefaultSharedPreferences(this);

		navController = Navigation.findNavController(this, R.id.main_fragment_holder);

		Set<Integer> topLevelDestinations = new HashSet<>();
		topLevelDestinations.add(R.id.navMain);
		topLevelDestinations.add(R.id.navTourList);
		AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
				topLevelDestinations).setDrawerLayout(drawer_layout).build();

		setSupportActionBar(toolbar);
		NavigationUI.setupWithNavController(navigationView, navController);
		NavigationUI.setupWithNavController(toolbar, navController, appBarConfiguration);

		loadDatabase();
	}

	private void loadDatabase() {
		new Thread(() -> {
			Log.i(TAG, "loadDatabaseOnStart ");
			User user = Preferences.getLoggedUser(sp);
			if(user != null && user.getId() > 0) { // is logged
				// load user plays
				TourRepository tourRepository = RepositoryFactory
						.getTourRepository(getApplication());
				tourRepository.getToursOnStart(user.getId());
				PlayRepository playRepository = RepositoryFactory
						.getPlayRepository(getApplication());
				playRepository.getPlayOnStart(user.getId());
				PlaceRepository placeRepository = RepositoryFactory
						.getPlaceRepository(getApplication());
				placeRepository.getPlaceOnStart(user.getId());
			}
		}).start();
	}

	public void setDrawerHeader(String username, String name) {
		View headerView = navigationView.getHeaderView(0);
		((TextView) headerView.findViewById(R.id.drawer_header_name)).setText(name);
		((TextView) headerView.findViewById(R.id.drawer_header_username)).setText(username);
	}

	public void disableToolbarAndDrawer(boolean visibility) {
		toolbar.setVisibility(visibility ?
				                      View.VISIBLE :
				                      View.GONE);
		drawer_layout.setDrawerLockMode(visibility ?
				                                DrawerLayout.LOCK_MODE_UNLOCKED :
				                                DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
	}

}
