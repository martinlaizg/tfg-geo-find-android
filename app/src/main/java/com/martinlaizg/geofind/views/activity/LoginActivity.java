package com.martinlaizg.geofind.views.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.views.model.LoginViewModel;

import java.util.Objects;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import butterknife.BindView;
import butterknife.ButterKnife;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Log tag
    private static final String TAG = LoginActivity.class.getSimpleName();

    // View elements

    @BindView(R.id.login_email_input_layout)
    TextInputLayout email_input;
    @BindView(R.id.login_password_input_layout)
    TextInputLayout password_input;

    @BindView(R.id.login_button)
    Button login_button;
    @BindView(R.id.login_register_button)
    Button registry_button;


    private LoginViewModel loginViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(LoginActivity.this);

        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);

        login_button.setOnClickListener(this);
        registry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        checkLogin();
    }

    private void checkLogin() {
        User user = Preferences.getLoggedUser(getApplicationContext());
        if (user != null) {
            Toast.makeText(this, getString(R.string.already_logged), Toast.LENGTH_SHORT).show();
            finish();
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
            Toast.makeText(this, getString(R.string.wrong_email_password), Toast.LENGTH_SHORT).show();
            return;
        }
        Preferences.setLoggedUser(getApplicationContext(), user);
        finish();
    }
}
