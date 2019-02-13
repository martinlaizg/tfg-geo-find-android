package com.martinlaizg.geofind.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.martinlaizg.geofind.R;

import androidx.appcompat.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.name_input)
    public EditText name;
    @BindView(R.id.email_input)
    public EditText email;
    @BindView(R.id.password_input)
    public EditText password;
    @BindView(R.id.c_password_input)
    public EditText c_password;

    @BindView(R.id.btn_registry)
    public Button btn_registr;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(RegisterActivity.this);

        btn_registr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                registry();
            }
        });
    }

    private void registry() {
        btn_registr.setVisibility(View.GONE);

        final String name = this.name.getText().toString().trim();
        final String email = this.email.getText().toString().trim();
        final String password = this.password.getText().toString().trim();

        final Toast toast1 = Toast.makeText(getApplicationContext(), "Registrado correctamente",
                Toast.LENGTH_LONG);
        toast1.show();


    }
}
