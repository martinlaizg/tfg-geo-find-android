package com.martinlaizg.geofind.views.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.config.Preferences;
import com.martinlaizg.geofind.dataAccess.database.entity.User;
import com.martinlaizg.geofind.dataAccess.retrofit.RestClient;
import com.martinlaizg.geofind.dataAccess.retrofit.RetrofitInstance;
import com.martinlaizg.geofind.dataAccess.retrofit.error.APIError;
import com.martinlaizg.geofind.dataAccess.retrofit.error.ErrorUtils;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(LoginActivity.this);

        initView();
        checkLogin();
    }

    private void initView() {
        login_button.setOnClickListener(this);
        registry_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

    }

    private void login(final String email, String password) {
        RestClient client = RetrofitInstance.getRestClient();
        User user = new User(email, password);
        client.login(user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {

                SharedPreferences sp = Preferences.getInstance(getApplicationContext());
                if (response.isSuccessful()) { // Response code 200->300
                    // Parse response to JSON
                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
                    String stringUser = gson.toJson(response.body());
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
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkLogin() {
        SharedPreferences sp = Preferences.getInstance(getApplicationContext());
        if (sp.getBoolean(Preferences.LOGGED, false)) {
            Toast.makeText(getApplicationContext(), "Ya estabas logeado", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public void onClick(View v) {
        try {
            if (TextUtils.isEmpty(email_input.getEditText().getText())) {
                email_input.setError(getString(R.string.required_email));
                return;
            }
            if (TextUtils.isEmpty(password_input.getEditText().getText())) {
                password_input.setError(getString(R.string.required_password));
                return;
            }
        } catch (NullPointerException ex) {
            Toast.makeText(getApplicationContext(), "Error de inputs", Toast.LENGTH_SHORT).show();
        }

        String email = email_input.getEditText().getText().toString();
        String password = password_input.getEditText().getText().toString();
        login(email, password);
    }
}
