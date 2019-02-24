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
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.activity.personal.create.MapCreatorActivity;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;

public class CreateMapFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.new_map_name_layout)
    TextInputLayout new_map_name;
    @BindView(R.id.new_map_description_layout)
    TextInputLayout new_map_description;

    @BindView(R.id.new_map_next)
    Button next_button;
    @BindView(R.id.new_map_add_image_button)
    Button add_image_button;


    public CreateMapFragment() {
        // Required empty public constructor
    }

    public static CreateMapFragment newInstance() {
        return new CreateMapFragment();
    }


    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_create_map, container, false);
        ButterKnife.bind(this, view);

        next_button.setOnClickListener(this);
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

        String name = new_map_name.getEditText().getText().toString();
        String description = new_map_description.getEditText().getText().toString();

        MapCreatorActivity parentActivity = (MapCreatorActivity) getActivity();
        parentActivity.map.setName(name);
        parentActivity.map.setDescription(description);
        User user = Preferences.getLoggedUser(getContext());
        parentActivity.map.setCreator_id(user.getId());

        final Fragment fragment = CreateLocationFragment.newInstance();
        final FragmentManager fragmentManager = getFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.map_creator_frame_layout, fragment, MapCreatorActivity.MAP_CREATOR_FRAGMENT);
        fragmentTransaction.commit();
    }
}
