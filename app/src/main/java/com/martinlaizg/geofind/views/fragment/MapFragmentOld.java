package com.martinlaizg.geofind.views.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
import com.martinlaizg.geofind.views.viewmodel.MapViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;

@Deprecated
public class MapFragmentOld extends Fragment implements OnMapReadyCallback {

    public static final String MAP_ID = "MAP_ID";

    private static final String TAG = MapFragmentOld.class.getSimpleName();

    // View
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

    private GoogleMap mMap;
    private MapViewModel mapViewModel;
    private NavController navController;
    private String map_id;

    public MapFragmentOld() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle b = getArguments();
        navController = Navigation.findNavController(getActivity(), R.id.main_fragment_holder);
        if (b == null || (map_id = b.getString(MAP_ID)) == null) {
            navController.popBackStack(R.id.map_list, false);
            return null;
        }
        // TODO add backbutton on app toolbar

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map_old, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mapView.onCreate(savedInstanceState);
        mapView.onResume(); // needed to get the map to display immediately
        mapView.getMapAsync(this);

        mapViewModel = ViewModelProviders.of(this).get(MapViewModel.class);
        mapViewModel.getMap(map_id).observe(this, new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                setMap(map);
            }
        });
        mapViewModel.getLocations(map_id).observe(this, new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                setMapLocation(locations);
            }
        });
    }

    private void setMap(Map map) {
        if (map != null) {
            mapName.setText(map.getName());
            mapLocation.setText(map.getCity());
            mapDescription.setText(map.getDescription());
            return;
        }
        Toast.makeText(getActivity(), "No se ha podido cargar el mapa", Toast.LENGTH_SHORT).show();
        navController.popBackStack();
    }

    private void setMapLocation(List<Location> map_locations) {
        if (map_locations == null) {
            Toast.makeText(getActivity(), "No se han encontrado localizaciones", Toast.LENGTH_SHORT).show();
            navController.popBackStack();
            return;
        }
        if (map_locations.size() > 0) {
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
            mMap.moveCamera(cu);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

}
