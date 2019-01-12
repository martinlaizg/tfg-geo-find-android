package com.martinlaizg.geofind.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.client.RetrofitInstance;
import com.martinlaizg.geofind.client.error.APIError;
import com.martinlaizg.geofind.client.error.ErrorUtils;
import com.martinlaizg.geofind.client.login.LoginRequest;
import com.martinlaizg.geofind.client.user.UserResponse;
import com.martinlaizg.geofind.config.Preferences;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    // Log tag
    private static final String TAG = LoginActivity.class.getSimpleName();

    // View elements
    private EditText emailInput, passwordInput;
    private Button loginButton;
    private Button goRegistryButton;

    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = Preferences.getInstance(getApplicationContext());

        initView();
        checkLogin();
    }

    private void initView() {
        // Inputs
        emailInput = findViewById(R.id.login_email_input);
        passwordInput = findViewById(R.id.login_password_input);

        // Buttons
        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(emailInput.getText().toString(), passwordInput.getText().toString());
            }
        });

        goRegistryButton = findViewById(R.id.login_register_btn);
        goRegistryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    private void login(final String email, String password) {
        RestClient client = RetrofitInstance.getRestClient();
        LoginRequest lreq = new LoginRequest(email, password);
        client.login(lreq).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful()) { // Response code 200->300
                    // Parse response to JSON
                    String stringUser = new Gson().toJson(response.body());
                    Toast.makeText(LoginActivity.this, "Login correcto", Toast.LENGTH_SHORT).show();
                    // Save status to preferences
                    sp.edit().putBoolean(Preferences.LOGGED, true).apply();
                    sp.edit().putString(Preferences.USER, stringUser).apply();
                    finish();
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                    Log.d(TAG, errorMessage);
                    sp.edit().putBoolean(Preferences.LOGGED, false).apply();
                    sp.edit().putString(Preferences.USER, "").apply();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLogin() {
        if (sp.getBoolean(Preferences.LOGGED, false)) {
            Toast.makeText(getApplicationContext(), "Ya estabas logeado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}
