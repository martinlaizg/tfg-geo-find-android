package com.martinlaizg.geofind.views.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.martinlaizg.geofind.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
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


	private NavController navController;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ButterKnife.bind(MainActivity.this);
		setupNavigation();
	}

	public void setDrawerHeader(String username, String name) {
		View headerView = navigationView.getHeaderView(0);
		((TextView) headerView.findViewById(R.id.drawer_header_name)).setText(name);
		((TextView) headerView.findViewById(R.id.drawer_header_username)).setText(username);
	}

	public void setToolbarVisibility(boolean visibility) {
		toolbar.setVisibility(visibility ?
				View.VISIBLE :
				View.GONE);
	}

	private void setupNavigation() {
		setSupportActionBar(toolbar);
		navController = Navigation.findNavController(this, R.id.main_fragment_holder);
		NavigationUI.setupWithNavController(toolbar, navController, drawer_layout);
		NavigationUI.setupWithNavController(navigationView, navController);
	}

}
