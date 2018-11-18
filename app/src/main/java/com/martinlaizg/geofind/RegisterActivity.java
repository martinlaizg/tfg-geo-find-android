package com.martinlaizg.geofind;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.StringReader;

public class RegisterActivity extends AppCompatActivity {

    private EditText name, email, password, c_password;
    private Button btn_registr;
    private ProgressBar loading;
    private static String URL_REGISTRY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        loading = findViewById(R.id.loading);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        btn_registr = findViewById(R.id.btn_registry);

        btn_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registry();
            }
        });

    }

    private void Registry() {
        loading.setVisibility(View.VISIBLE);
        btn_registr.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        Toast toast1 = Toast.makeText(getApplicationContext(),"Registrado correctamente", Toast.LENGTH_LONG);
        toast1.show();



    }
}
