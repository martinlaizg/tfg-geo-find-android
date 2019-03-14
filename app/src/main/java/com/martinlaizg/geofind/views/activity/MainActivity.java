package com.martinlaizg.geofind.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG = MainActivity.class.getSimpleName();

    @BindView(R.id.main_drawer_layout)
    DrawerLayout drawer_layout;
    @BindView(R.id.main_toolbar)
    Toolbar toolbar;

    @BindView(R.id.drawer_nav_view)
    NavigationView navigationView;

    private NavController navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        setUpToolbar();
        setUpNavigation();
        checkLogin();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer_layout, toolbar, R.string.open, R.string.close);
        drawer_layout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void setUpNavigation() {
        navigation = Navigation.findNavController(this, R.id.main_fragment_holder);
        navigationView.setNavigationItemSelectedListener(this);
    }

    private void checkLogin() {
        User loggedUser = Preferences.getLoggedUser(getApplicationContext());
        if (loggedUser == null || loggedUser.getEmail().isEmpty()) {
            Log.i(TAG, "User not logged");
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            return;
        }

        View headerView = navigationView.getHeaderView(0);
        TextView name = headerView.findViewById(R.id.drawer_header_name);
        TextView username = headerView.findViewById(R.id.drawer_header_username);
        name.setText(loggedUser.getName());
        username.setText(loggedUser.getUsername());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        uncheckMenuItems();
        menuItem.setChecked(true);

        // If the current destination is not the main, return to there
        NavDestination currentDestination = navigation.getCurrentDestination();
        if (currentDestination != null && currentDestination.getId() != R.id.main) {
            navigation.popBackStack(R.id.main, false);
        }
        drawer_layout.closeDrawers();
        switch (menuItem.getItemId()) {
            case R.id.menu_maps:
                Toast.makeText(this, "Cargar mapas", Toast.LENGTH_SHORT).show();
                navigation.navigate(R.id.toMapList);
                break;
            case R.id.menu_locations:
                Toast.makeText(this, "Cargar localizaciones", Toast.LENGTH_SHORT).show();
                navigation.navigate(R.id.toLocationList);
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Ir a ajustes", Toast.LENGTH_SHORT).show();
                navigation.navigate(R.id.toAccount);
                menuItem.setChecked(false);
                break;
            case R.id.menu_log_out:
                Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
                logOut();
                menuItem.setChecked(false);
                break;
        }
        return true;
    }

    private void uncheckMenuItems() {
        for (int i = 0; i < navigationView.getMenu().size(); i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void logOut() {
        Preferences.setLoggedUser(this, null);
        checkLogin();
    }

    @Override
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            uncheckMenuItems();
            super.onBackPressed();
        }
    }
}
