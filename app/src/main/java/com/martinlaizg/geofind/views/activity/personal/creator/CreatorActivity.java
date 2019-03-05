package com.martinlaizg.geofind.views.activity.personal.creator;

import android.os.Bundle;
import android.view.MenuItem;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.model.MapCreatorViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;
import butterknife.ButterKnife;

public class CreatorActivity extends AppCompatActivity {

    public static final String MAP_CREATOR_FRAGMENT = "MAP_CREATOR_FRAGMENT";
    public Map map;
    public List<Location> locations;

    MapCreatorViewModel mapCreatorViewModel;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_creator);
        ButterKnife.bind(CreatorActivity.this);

        mapCreatorViewModel = ViewModelProviders.of(this).get(MapCreatorViewModel.class);


        // Load the main fragment
        CreatorMainFragment fragment = CreatorMainFragment.newInstance();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_creator_frame_layout, fragment, MAP_CREATOR_FRAGMENT);
        fragmentTransaction.commit();
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
