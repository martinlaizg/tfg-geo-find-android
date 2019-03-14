package com.martinlaizg.geofind.views.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.views.viewmodel.LocationViewModel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationFragment extends Fragment {


    public static final String LOC_ID = "LOCATION_ID";

    @BindView(R.id.location_name)
    TextView location_name;
    @BindView(R.id.location_description)
    TextView location_description;
    @BindView(R.id.location_visits)
    TextView location_visits;
    @BindView(R.id.location_image)
    ImageView location_image;

    private String loc_id;
    private LocationViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location, container, false);
        ButterKnife.bind(this, view);
        Bundle b = getArguments();
        if (b != null) {
            loc_id = b.getString(LOC_ID);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = ViewModelProviders.of(this).get(LocationViewModel.class);

        viewModel.getLocation(loc_id).observe(this, new Observer<Location>() {
            @Override
            public void onChanged(Location location) {
                setLocation(location);
            }
        });
    }

    private void setLocation(Location location) {
        if (location != null) {
            location_name.setText(location.getName());
            location_description.setText("Descripción pendiente TODO"); // TODO cambiar por el valor real
            location_visits.setText("2.594 TODO"); // TODO cambiar por el valor real
            location_image.setImageResource(R.drawable.default_map_image);
        }
    }
}
