package com.martinlaizg.geofind.views.activity.personal.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.activity.MapActivity;
import com.martinlaizg.geofind.views.activity.personal.create.map.CreateMapFragment;
import com.martinlaizg.geofind.views.model.MapCreatorViewModel;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapCreatorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MAP_CREATOR_FRAGMENT = "MAP_CREATOR_FRAGMENT";
    public Map map;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    MapCreatorViewModel mapCreatorViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_creator);
        ButterKnife.bind(MapCreatorActivity.this);

        mapCreatorViewModel = ViewModelProviders.of(this).get(MapCreatorViewModel.class);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Initial fragment
        final CreateMapFragment fragment = CreateMapFragment.newInstance();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_creator_frame_layout, fragment, MAP_CREATOR_FRAGMENT);
        fragmentTransaction.commit();
    }

    @Override
    public void onClick(View v) {

        mapCreatorViewModel.insert(map);

        Intent intent = new Intent(getApplicationContext(), MapActivity.class);
        intent.putExtra(MapActivity.MAP_ID, map.getId());
        startActivity(intent);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
