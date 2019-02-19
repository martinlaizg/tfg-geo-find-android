package com.martinlaizg.geofind.views.activity.personal.create.map;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.views.activity.personal.create.MapCreatorActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;


public class CreateLocationFragment extends Fragment implements View.OnClickListener {


    private static final String MAP_NAME = "MAP_NAME";
    private static final String MAP_DESCRIPTION = "MAP_DESCRIPTION";

    @BindView(R.id.new_location_name_layout)
    TextInputLayout new_location_name;
    @BindView(R.id.new_location_lat_layout)
    TextInputLayout new_location_lat;
    @BindView(R.id.new_location_lon_layout)
    TextInputLayout new_location_lon;

    @BindView(R.id.new_location_next)
    Button next_button;
    @BindView(R.id.finish_creator)
    Button finish_button;


    public CreateLocationFragment() {
        // Required empty public constructor
    }

    static CreateLocationFragment newInstance() {
        return new CreateLocationFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_create_locations, container, false);
        ButterKnife.bind(this, view);
        next_button.setOnClickListener(this);
        finish_button.setOnClickListener((MapCreatorActivity) getActivity());
        return view;
    }

    @Override
    public void onClick(View v) {
        try {
            if (TextUtils.isEmpty(new_location_name.getEditText().getText())) {
                new_location_name.setError(getString(R.string.required_name));
                return;
            }
            if (TextUtils.isEmpty(new_location_lat.getEditText().getText())) {
                new_location_lat.setError(getString(R.string.required_lat));
                return;
            }
            if (TextUtils.isEmpty(new_location_lon.getEditText().getText())) {
                new_location_lon.setError(getString(R.string.required_lon));
                return;
            }
        } catch (NullPointerException ex) {
            Toast.makeText(getContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        String name = new_location_name.getEditText().getText().toString();
        String lat = new_location_lat.getEditText().getText().toString();
        String lon = new_location_lon.getEditText().getText().toString();


        MapCreatorActivity parentActivity = (MapCreatorActivity) getActivity();
        Toast.makeText(parentActivity, "Localización creada", Toast.LENGTH_SHORT).show();

        final Fragment fragment = getFragmentManager().findFragmentByTag(MapCreatorActivity.MAP_CREATOR_FRAGMENT);
        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();

    }
}
