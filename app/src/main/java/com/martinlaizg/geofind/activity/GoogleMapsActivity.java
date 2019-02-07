package com.martinlaizg.geofind.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.client.RetrofitInstance;
import com.martinlaizg.geofind.client.error.APIError;
import com.martinlaizg.geofind.client.error.ErrorUtils;
import com.martinlaizg.geofind.entity.Location;
import com.martinlaizg.geofind.entity.Maps;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Value to get destination map
    public static final String MAP_ID_KEY = "map_id_key";
    private static final String TAG = GoogleMapsActivity.class.getSimpleName();

    private GoogleMap mMap;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.map_title)
    TextView mapTitle;
    @BindView(R.id.map_location)
    TextView mapLocation;
    @BindView(R.id.map_description)
    TextView mapDescription;
    @BindView(R.id.map)
    MapView mapView;


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

        restClient.getSingleMap(map_id).enqueue(new Callback<Maps>() {
            @Override
            public void onResponse(Call<Maps> call, Response<Maps> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    Toast.makeText(GoogleMapsActivity.this, "Carga de mapa correcto", Toast.LENGTH_SHORT).show();
                    Maps map = response.body();
                    mapTitle.setText(map.getName());
                    mapLocation.setText(map.getCity());
                    mapDescription.setText(map.getCity());

                    setMapLocation(map.getLocations());
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                }
            }

            @Override
            public void onFailure(Call<Maps> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    void setMapLocation(List<Location> map_locations) {
        Double latSum = 0.0;
        Double lonSum = 0.0;
        for (Location loc : map_locations) {
            Double lat = Double.valueOf(loc.getLat());
            latSum += lat;
            Double lon = Double.valueOf(loc.getLon());
            lonSum += lon;
            LatLng location = new LatLng(lat, lon);
            mMap.addMarker(new MarkerOptions().position(location).title(loc.getName()));
        }
        double latCamera = latSum / map_locations.size();
        double lonCamera = lonSum / map_locations.size();

        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latCamera, lonCamera)));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Set padding bottom for show Google logo on map
        int radius = (int) getResources().getDimension(R.dimen.map_card_corner_radius);
        mMap.setPadding(0, 0, 0, radius);
        // Add a marker in Sydney and move the camera
        LatLng location = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
