package com.martinlaizg.geofind.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.auth.Auth;
import com.martinlaizg.geofind.config.Preferences;

public class LoginActivity extends AppCompatActivity {

    // View elements
    private EditText emailInput, passwordInput;
    private Button loginButton;

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

        // Button
        loginButton = findViewById(R.id.login_btn);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean logged = Auth.login(emailInput.getText().toString(), passwordInput.getText().toString());
                sp.edit().putBoolean(Preferences.LOGGED, logged).apply();
                if (logged) {
                    Toast.makeText(getApplicationContext(), "Logeado correctamente", Toast.LENGTH_SHORT).show();
                    finish();
                    return;
                }
                // TODO: mostrar mensaje de error de login
                Toast.makeText(getApplicationContext(), "Error al logear", Toast.LENGTH_LONG).show();
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
