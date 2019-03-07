package com.martinlaizg.geofind.views.fragment.creator;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.CreatorLocationAdapter;
import com.martinlaizg.geofind.data.access.database.entity.Location;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.views.model.MapCreatorViewModel;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreatorFragment extends Fragment {

    // View
    @BindView(R.id.create_map_name)
    TextView mapName;
    @BindView(R.id.create_map_description)
    TextView mapDescription;

    @BindView(R.id.add_location_button)
    Button add_location_button;
    @BindView(R.id.edit_button)
    Button editButton;

    @BindView(R.id.rec_view_loc_list)
    RecyclerView recyclerView;

    private MapCreatorViewModel creatorViewModel;
    private CreatorLocationAdapter adapter;
    private NavController navController;

    public CreatorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_creator, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(getActivity(), R.id.main_fragment_holder);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new CreatorLocationAdapter();
        recyclerView.setAdapter(adapter);

        add_location_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.toCreateLocation);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.popBackStack();
            }
        });

        creatorViewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);

        if (!creatorViewModel.isMapCreated()) {
            mapName.setText("Mapa no creado");
            mapDescription.setText("");
            // disable done button
        }

        creatorViewModel.getCreatedMap().observe(getActivity(), new Observer<Map>() {
            @Override
            public void onChanged(Map map) {
                if (!map.getName().isEmpty()) {
                    mapName.setText(map.getName());
                }
                if (!map.getDescription().isEmpty()) {
                    mapDescription.setText(map.getDescription());
                }
            }
        });

        creatorViewModel.getCreatedLocations().observe(getActivity(), new Observer<List<Location>>() {
            @Override
            public void onChanged(List<Location> locations) {
                adapter.setLocations(locations);
            }
        });
    }


}
