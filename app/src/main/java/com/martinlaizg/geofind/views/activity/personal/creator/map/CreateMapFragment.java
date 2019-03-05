package com.martinlaizg.geofind.views.activity.personal.creator.map;


import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.activity.personal.creator.CreatorActivity;
import com.martinlaizg.geofind.views.model.MapCreatorViewModel;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateMapFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.new_map_name_layout)
    TextInputLayout new_map_name;
    @BindView(R.id.new_map_description_layout)
    TextInputLayout new_map_description;

    @BindView(R.id.done_button)
    Button doneButton;
    @BindView(R.id.return_button)
    Button returnButton;
    @BindView(R.id.add_image_button)
    Button add_image_button;

    MapCreatorViewModel mapCreatorViewModel;

    public CreateMapFragment() {
        // Required empty public constructor
    }

    public static CreateMapFragment newInstance() {
        return new CreateMapFragment();
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_map, container, false);
        ButterKnife.bind(this, view);

        mapCreatorViewModel = ViewModelProviders.of(getActivity()).get(MapCreatorViewModel.class);

        doneButton.setOnClickListener(this);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getFragmentManager() != null) {
                    getFragmentManager().popBackStack();
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(final View v) {

        try {
            if (TextUtils.isEmpty(new_map_name.getEditText().getText())) {
                new_map_name.setError(getString(R.string.required_name));
                return;
            }
            if (TextUtils.isEmpty(new_map_description.getEditText().getText())) {
                new_map_description.setError(getString(R.string.required_description));
                return;
            }
        } catch (NullPointerException ex) {
            Toast.makeText(getContext(), "View incorrecto", Toast.LENGTH_SHORT).show();
            return;
        }

        String name = new_map_name.getEditText().getText().toString().trim();
        String description = new_map_description.getEditText().getText().toString().trim();

        Map map = new Map();
        map.setName(name);
        map.setDescription(description);
        User user = Preferences.getLoggedUser(getContext());
        map.setCreator_id(user.getId());
        CreatorActivity parentActivity = (CreatorActivity) getActivity();
        mapCreatorViewModel.setMap(map);
        if (getFragmentManager() != null) {
            getFragmentManager().popBackStack();
        }
    }
}
