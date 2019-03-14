package com.martinlaizg.geofind;

import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayLocationFragment extends Fragment implements OnMapReadyCallback {

    private static final String TAG = PlayLocationFragment.class.getSimpleName();
    private static final long MIN_TIME = 500;
    private static final float MIN_DISTANCE = 1000;

    @BindView(R.id.location_name)
    TextView location_name;
    @BindView(R.id.location_description)
    TextView location_description;

    @BindView(R.id.mapView)
    MapView mapView;

    private LocationManager locationManager;
    private GoogleMap mMap;

    public PlayLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_location, container, false);
        ButterKnife.bind(this, view);
        mapView.onCreate(savedInstanceState);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        // Set the viewModel
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }
}
