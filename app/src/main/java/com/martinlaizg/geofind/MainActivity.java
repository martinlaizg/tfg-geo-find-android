package com.martinlaizg.geofind;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.activity.LoginActivity;
import com.martinlaizg.geofind.views.activity.personal.MyAccountActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        setUpNavigation();
        setUpToolbar();
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
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getHeaderView(0).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Ir a perfil del usuario", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), MyAccountActivity.class));
            }
        });

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
    public void onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        menuItem.setChecked(true);

        drawer_layout.closeDrawers();
        NavController navController = Navigation.findNavController(this, R.id.main_fragment_holder);

        switch (menuItem.getItemId()) {
            case R.id.menu_users:
                Toast.makeText(this, "Cargar usuarios", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_maps:
                Toast.makeText(this, "Cargar mapas", Toast.LENGTH_SHORT).show();
                navController.navigate(R.id.toMapListFragment);
                break;
            case R.id.menu_locations:
                Toast.makeText(this, "Cargar localizaciones", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_settings:
                Toast.makeText(this, "Ir a ajustes", Toast.LENGTH_SHORT).show();
                break;
            case R.id.menu_log_out:
                Toast.makeText(this, "Cerrar sesión", Toast.LENGTH_SHORT).show();
                logOut();
                break;
        }

        return true;
    }

    private void logOut() {
        Preferences.setLoggedUser(this, null);
        checkLogin();
    }
}
