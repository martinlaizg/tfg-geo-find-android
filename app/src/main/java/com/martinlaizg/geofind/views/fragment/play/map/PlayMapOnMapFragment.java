package com.martinlaizg.geofind.views.fragment.play.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayMapOnMapFragment extends Fragment implements OnMapReadyCallback {

    private static final int MAP_PADDING = 80;
    @BindView(R.id.map_name)
    TextView map_name;
    @BindView(R.id.map_description)
    TextView map_description;

    @BindView(R.id.map_view)
    MapView map_view;
    private MapViewModel viewModel;
    private Map map;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_play_map_on_map, container, false);
        ButterKnife.bind(this, view);
        map_view.onCreate(savedInstanceState);
        map_view.onResume();
        map_view.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(getActivity()).get(MapViewModel.class);
        map = viewModel.getMap();
        map_name.setText(map.getName());
        map_description.setText(map.getDescription());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        List<Location> locs = viewModel.getLocations();
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (Location l : locs) {
            MarkerOptions m = new MarkerOptions().position(l.getLatLng()).title(l.getName());
            googleMap.addMarker(m);
            builder.include(m.getPosition());
        }
        LatLngBounds bounds = builder.build();
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, MAP_PADDING);
        googleMap.animateCamera(cu);
    }
}
