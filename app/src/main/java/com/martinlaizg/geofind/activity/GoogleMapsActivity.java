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
import com.martinlaizg.geofind.entity.Maps;

import java.util.HashMap;
import java.util.Map;

import androidx.fragment.app.FragmentActivity;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // Value to get destination map
    public static final String MAP_ID_KEY = "map_id_key";
    private static final String TAG = GoogleMapsActivity.class.getSimpleName();

    private Integer map_id;

    private GoogleMap mMap;

    private TextView mapTitle;
    private TextView mapLocation;
    private TextView mapDescription;
    private LatLng location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        MapView mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately

        mapView.getMapAsync(this);
        String id = getIntent().getStringExtra(MAP_ID_KEY);

        map_id = Integer.parseInt(id);
        mapTitle = findViewById(R.id.map_title);
        mapLocation = findViewById(R.id.map_location);
        mapDescription = findViewById(R.id.map_description);

        loadMap(map_id);
    }

    private void loadMap(Integer map_id) {
        RestClient restClient = RetrofitInstance.getRestClient();
        Map<String, String> parameters = new HashMap<>();

        restClient.getSingleMap(map_id.toString()).enqueue(new Callback<Maps>() {
            @Override
            public void onResponse(Call<Maps> call, Response<Maps> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    Toast.makeText(GoogleMapsActivity.this, "Carga de mapa correcto", Toast.LENGTH_SHORT).show();

                    Maps map = response.body();
                    mapTitle.setText(map.getName());
                    mapLocation.setText(map.getCity());
                    mapDescription.setText(map.getCity());
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


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        location = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(location).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(location));
    }
}
