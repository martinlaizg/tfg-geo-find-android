package com.martinlaizg.geofind.activity.personal.create;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.activity.personal.create.map.CreateLocations;
import com.martinlaizg.geofind.activity.personal.create.map.CreateMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapCreatorActivity extends AppCompatActivity {

    private final String FRAGMENT_TAG = "FRAGMENT";

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.new_map_next)
    Button next_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_creator);
        ButterKnife.bind(MapCreatorActivity.this);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Initial fragment
        CreateMapFragment fragment = CreateMapFragment.newInstance();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_creator_frame_layout, fragment, FRAGMENT_TAG);
        fragmentTransaction.commit();

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView txtv = findViewById(R.id.new_map_name);
                String name = txtv.getText().toString();
                txtv = findViewById(R.id.new_map_description);
                String desc = txtv.getText().toString();

                Fragment fragment = CreateLocations.newInstance(name, desc);
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.map_creator_frame_layout, fragment)
                        .commit();
            }
        });


    }

}
