package com.martinlaizg.geofind.views.fragment.creator;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.views.viewmodel.MapCreatorViewModel;

import org.jetbrains.annotations.NotNull;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateLocationFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback {

    public static final int CAMERA_UPDATE_ZOOM = 15;
    private static final String MAP_NAME = "MAP_NAME";
    private static final String MAP_DESCRIPTION = "MAP_DESCRIPTION";

    @BindView(R.id.new_location_name_layout)
    TextInputLayout new_location_name;
    @BindView(R.id.alert_no_locaiton_text)
    TextView alert_no_locaiton_text;

    @BindView(R.id.create_location)
    Button create_button;

    @BindView(R.id.new_location_map_view)
    MapView new_location_map_view;

    private MapCreatorViewModel viewModel;
    private MarkerOptions marker;

    public CreateLocationFragment() {
        // Required empty public constructor
    }

    public static CreateLocationFragment newInstance() {
        return new CreateLocationFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_locations, container, false);
        ButterKnife.bind(this, view);
        new_location_map_view.onCreate(savedInstanceState);
        new_location_map_view.onResume();
        new_location_map_view.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);
        create_button.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        alert_no_locaiton_text.setVisibility(View.GONE);
        try {
            if (TextUtils.isEmpty(new_location_name.getEditText().getText())) {
                new_location_name.setError(getString(R.string.required_name));
                return;
            }
            if (marker == null) {
                alert_no_locaiton_text.setVisibility(View.VISIBLE);
                return;
            }
        } catch (NullPointerException ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }


        String name = new_location_name.getEditText().getText().toString().trim();
        viewModel.addLocation(name,
                String.valueOf(marker.getPosition().latitude),
                String.valueOf(marker.getPosition().longitude));


        Navigation.findNavController(getActivity(), R.id.main_fragment_holder).popBackStack();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        GoogleMap gMap = googleMap;


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && getActivity().checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(getActivity(), getString(R.string.no_location_permissions), Toast.LENGTH_SHORT).show();
            return;
        }
        Location usrLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        gMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                alert_no_locaiton_text.setVisibility(View.GONE);
                MarkerOptions m = new MarkerOptions().position(latLng);
                gMap.clear();
                gMap.addMarker(m);
                marker = m;
            }
        });
        gMap.setMyLocationEnabled(true);
        gMap.getUiSettings().setMyLocationButtonEnabled(true);
        gMap.getUiSettings().setZoomControlsEnabled(true);
        gMap.getUiSettings().setMapToolbarEnabled(false);
        gMap.getUiSettings().setTiltGesturesEnabled(false);
        gMap.getUiSettings().setScrollGesturesEnabledDuringRotateOrZoom(false);

        LatLng usrLatLng = new LatLng(usrLocation.getLatitude(), usrLocation.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(usrLatLng, CAMERA_UPDATE_ZOOM);
        gMap.animateCamera(cameraUpdate);
    }

}
