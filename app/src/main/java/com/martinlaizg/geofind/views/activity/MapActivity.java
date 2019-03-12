package com.martinlaizg.geofind.views.activity;

import android.os.Bundle;

import com.martinlaizg.geofind.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity {

    private static final String TAG = MapActivity.class.getSimpleName();

    private final String MAP_ID = "MAP_ID";

    @BindView(R.id.map_toolbar)
    Toolbar toolbar;

    private NavController navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(MapActivity.this);
        setUpToolbar();
        setupNavigation();
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupNavigation() {
        navigation = Navigation.findNavController(this, R.id.map_fragment_holder);
    }
}
