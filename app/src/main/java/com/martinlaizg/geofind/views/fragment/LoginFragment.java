package com.martinlaizg.geofind.views.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.activity.MainActivity;
import com.martinlaizg.geofind.views.fragment.list.LocationListFragment;
import com.martinlaizg.geofind.views.model.LoginViewModel;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import butterknife.BindView;
import butterknife.ButterKnife;


public class LoginFragment extends Fragment implements View.OnClickListener {
    // Bind view
    @BindView(R.id.email_input)
    TextInputLayout email_input;
    @BindView(R.id.password_input)
    TextInputLayout password_input;
    @BindView(R.id.login_button)
    Button login_button;
    @BindView(R.id.login_register_button)
    Button registry_button;
    private String TAG = LocationListFragment.class.getSimpleName();
    // View Model
    private LoginViewModel loginViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
        return fragment;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        // Bind view
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get ViewModel
        loginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewModel.class);
        login_button.setOnClickListener(this);
        registry_button.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toRegistry));
        checkLogin();
    }


    private void checkLogin() {
        User user = Preferences.getLoggedUser(getActivity());
        if (user != null) {
            Toast.makeText(getActivity(), getString(R.string.already_logged), Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (TextUtils.isEmpty(Objects.requireNonNull(email_input.getEditText()).getText())) {
                email_input.setError(getString(R.string.required_email));
                return;
            }
            if (TextUtils.isEmpty(Objects.requireNonNull(password_input.getEditText()).getText())) {
                password_input.setError(getString(R.string.required_password));
                return;
            }
        } catch (NullPointerException ex) {
            Log.e(TAG, "login: ", ex);
        }

        String email = email_input.getEditText().getText().toString();
        String password = password_input.getEditText().getText().toString();
        User user = loginViewModel.login(email, password);
        if (user == null) {
            Toast.makeText(getActivity(), getString(R.string.wrong_email_password), Toast.LENGTH_SHORT).show();
            return;
        }
        Preferences.setLoggedUser(getActivity(), user);
        Intent intent = new Intent(getActivity(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
