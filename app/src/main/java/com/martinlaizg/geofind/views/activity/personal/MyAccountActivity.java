package com.martinlaizg.geofind.views.activity.personal;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.views.activity.personal.create.MapCreatorActivity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MyAccountActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.create_map_button)
    Button create_map_button;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ButterKnife.bind(MyAccountActivity.this);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        create_map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final Intent intent = new Intent(getApplicationContext(),
                        MapCreatorActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return true;
    }
}
