package com.martinlaizg.geofind.views.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.model.MapListViewModel;
import com.martinlaizg.geofind.views.model.MapViewModel;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Value to get destination map
    public static final String MAP_ID = "map_id";
    private static final String TAG = MapActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.map_name)
    TextView mapName;
    @BindView(R.id.map_location)
    TextView mapLocation;
    @BindView(R.id.map_description)
    TextView mapDescription;

    @BindView(R.id.map_card_view)
    CardView cardView;

    @BindView(R.id.map)
    MapView mapView;

    MapListViewModel mapListViewModel;

    private GoogleMap mMap;
    private MapViewModel mapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
        mapView.getMapAsync(this);
        String map_id = getIntent().getStringExtra(MAP_ID);

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        Map map = mapViewModel.getMap(map_id);
        setMap(map);

        mapListViewModel = ViewModelProviders.of(this).get(MapListViewModel.class);

    }

    private void setMap(Map map) {
        mapName.setText(map.getName());
        mapLocation.setText(map.getCity());
        mapDescription.setText(map.getDescription());
    }

    void setMapLocation(List<Location> map_locations) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        for (Location loc : map_locations) {
            // Get lat and lon
            Double lat = Double.valueOf(loc.getLat());
            Double lon = Double.valueOf(loc.getLon());
            // Create location object
            LatLng location = new LatLng(lat, lon);
            // Add marker to de map
            mMap.addMarker(new MarkerOptions().position(location).title(loc.getName()));
            // Add location for CameraUpdate
            builder.include(location);
        }
        LatLngBounds bounds = builder.build();

        int width = getResources().getDisplayMetrics().widthPixels;
        int padding = (int) (width * 0.10); // offset from edges of the map 10% of screen

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        mMap.animateCamera(cu);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Set padding bottom for show Google logo on map
//        int radius = (int) getResources().getDimension(R.dimen.map_card_corner_radius);
//        mMap.setPadding(0, 0, 0, radius);
    }
}
