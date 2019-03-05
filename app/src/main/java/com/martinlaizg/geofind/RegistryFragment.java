package com.martinlaizg.geofind;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class RegistryFragment extends Fragment {


    // View elements
    // Inputs
    @BindView(R.id.name_input)
    public EditText name;
    @BindView(R.id.email_input)
    public EditText email;
    @BindView(R.id.password_input)
    public EditText password;
    @BindView(R.id.c_password_input)
    public EditText c_password;
    // Buttons
    @BindView(R.id.btn_registry)
    public Button btn_registr;

    public RegistryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registry, container, false);

        ButterKnife.bind(this, view);
        btn_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                registry();
            }
        });
        return view;
    }

    private void registry() {
        btn_registr.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        Toast.makeText(getActivity(), "Registrado correctamente", Toast.LENGTH_LONG).show();
        // TODO: implement registry
    }

}
