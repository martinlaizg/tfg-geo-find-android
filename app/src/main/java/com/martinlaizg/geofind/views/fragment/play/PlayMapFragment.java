package com.martinlaizg.geofind.views.fragment.play;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.enums.PlayLevel;
import com.martinlaizg.geofind.views.viewmodel.LocationViewModel;

import java.math.BigDecimal;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayMapFragment extends Fragment implements OnMapReadyCallback {

    public static final String LOC_ID = "LOC_ID";
    private static final String TAG = PlayMapFragment.class.getSimpleName();
    private static final long MIN_TIME = 1000;
    private static final float MIN_DISTANCE = 20;

    @BindView(R.id.location_name)
    TextView location_name;
    @BindView(R.id.location_description)
    TextView location_description;
    @BindView(R.id.location_distance)
    TextView location_distance;

    @BindView(R.id.mapView)
    MapView mapView;

    private LocationManager locationManager;
    private String loc_id;
    private PlayLevel play_mode;
    private LocationViewModel viewModel;

    private GoogleMap mMap;
    private LatLng latLng;
    private Location place;
    private android.location.Location usrLoc;
    private android.location.Location placeLoc;

    public PlayMapFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_map_location, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle b = getArguments();
        if (b != null) {
            loc_id = b.getString(LOC_ID);
        }

        mapView.onResume();
        mapView.getMapAsync(this);

        viewModel = ViewModelProviders.of(getActivity()).get(LocationViewModel.class);
        viewModel.getLocation(loc_id).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location l) {
                setLocation(l);
            }
        });

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        LocationListener locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(android.location.Location usrLocation) {
                Toast.makeText(getActivity(), "locationChanged", Toast.LENGTH_SHORT).show();
                LatLngBounds.Builder bld = new LatLngBounds.Builder();
                bld.include(new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude()));
                bld.include(latLng);
                LatLngBounds bounds = bld.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 70));
                String distance = getDistance(usrLocation.distanceTo(place.getAndroidLocation()));
                location_distance.setText(distance);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Toast.makeText(getActivity(), "onStatusChanged", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderEnabled(String provider) {
                Toast.makeText(getActivity(), "onProviderEnabled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onProviderDisabled(String provider) {
                Toast.makeText(getActivity(), "onProviderDisabled", Toast.LENGTH_SHORT).show();
            }
        };
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME, MIN_DISTANCE, locationListener);
    }

    private String getDistance(float distanceTo) {
        String mesure = "m";
        if (distanceTo > 1000) {
            distanceTo /= (float) 1000;
            mesure = "km";
        }
        BigDecimal bd = new BigDecimal(distanceTo);
        bd = bd.setScale(2, BigDecimal.ROUND_HALF_UP);
        return String.valueOf(bd.floatValue()) + mesure;
    }

    private void setLocation(Location l) {
        place = l;
        latLng = l.getLatLng();
        location_name.setText(l.getName());
        location_description.setText(l.getDescription());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        mMap.clear();
        mMap.addMarker(markerOptions);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setAllGesturesEnabled(false);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), "Location permission denied", Toast.LENGTH_SHORT).show();
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }
}
