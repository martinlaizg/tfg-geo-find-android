package com.martinlaizg.geofind;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;


public class AccountFragment extends Fragment {

    private static final String TAG = AccountFragment.class.getSimpleName();

    @BindView(R.id.account_user_name)
    TextView user_name;

    @BindView(R.id.create_map_button)
    Button create_map_button;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        User user = Preferences.getLoggedUser(getActivity());
        user_name.setText(user.getName());

        create_map_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toCreateMap));
    }
}
