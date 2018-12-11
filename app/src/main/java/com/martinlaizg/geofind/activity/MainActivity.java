package com.martinlaizg.geofind.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.UserAdapter;
import com.martinlaizg.geofind.config.Preferences;

public class MainActivity extends AppCompatActivity {

    // Other
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    // Drawer
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    // SharedPreferences
    private SharedPreferences sp;
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sp = Preferences.getInstance(getApplicationContext());
        initView();
        checkLogin();
    }

    private void initView() {
        drawer = findViewById(R.id.drawer_layout);

        // Activar la apertura y cierre del panel lateral
        toggle = new ActionBarDrawerToggle(this, drawer, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        navigationView = findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.nav_users:
                        Toast.makeText(getApplicationContext(), "List users", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_maps:
                        Toast.makeText(getApplicationContext(), "List maps", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_locations:
                        Toast.makeText(getApplicationContext(), "List locations", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_settings:
                        Toast.makeText(getApplicationContext(), "Go settings", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.nav_log_out:
                        Toast.makeText(getApplicationContext(), "Log out", Toast.LENGTH_SHORT).show();
                        sp.edit().putBoolean(Preferences.LOGGED, false).apply();
                        break;
                    default:
                        Toast.makeText(getApplicationContext(), "Default item", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;

            }
        });


    }

    private void checkLogin() {
        if (!sp.getBoolean(Preferences.LOGGED, false)) {
            Log.i(LOG_TAG, "User not logged");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }
}
