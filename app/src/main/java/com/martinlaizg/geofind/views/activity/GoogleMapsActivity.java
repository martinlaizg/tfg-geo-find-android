package com.martinlaizg.geofind.views.activity;

import android.os.Bundle;
import android.util.Log;
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
import com.martinlaizg.geofind.dataAccess.database.entity.Location;
import com.martinlaizg.geofind.dataAccess.retrofit.RestClient;
import com.martinlaizg.geofind.dataAccess.retrofit.RetrofitInstance;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Value to get destination map
    public static final String MAP_ID_KEY = "map_id_key";
    private static final String TAG = GoogleMapsActivity.class.getSimpleName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.map_title)
    TextView mapTitle;
    @BindView(R.id.map_location)
    TextView mapLocation;
    @BindView(R.id.map_description)
    TextView mapDescription;

    @BindView(R.id.map_card_view)
    CardView cardView;

    @BindView(R.id.map)
    MapView mapView;

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);
        ButterKnife.bind(this);

        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        mapView.getMapAsync(this);
        String id = getIntent().getStringExtra(MAP_ID_KEY);

        loadMap(id);
    }

    private void loadMap(String map_id) {
        RestClient restClient = RetrofitInstance.getRestClient();
        Map<String, String> parameters = new HashMap<>();

        try {

        } catch (Exception ex) {
            Log.d(TAG, "loadMap: " + ex.getMessage());
        }

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
