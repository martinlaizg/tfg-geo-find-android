package com.martinlaizg.geofind.views.activity.personal.create;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.martinlaizg.geofind.MainActivity;
import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.dataAccess.database.entity.Maps;
import com.martinlaizg.geofind.dataAccess.retrofit.RestClient;
import com.martinlaizg.geofind.dataAccess.retrofit.RetrofitInstance;
import com.martinlaizg.geofind.dataAccess.retrofit.error.APIError;
import com.martinlaizg.geofind.dataAccess.retrofit.error.ErrorUtils;
import com.martinlaizg.geofind.views.activity.personal.create.map.CreateMapFragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapCreatorActivity extends AppCompatActivity implements View.OnClickListener {

    public static final String MAP_CREATOR_FRAGMENT = "MAP_CREATOR_FRAGMENT";
    public Maps map;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_creator);
        ButterKnife.bind(MapCreatorActivity.this);

        // Toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        // Initial fragment
        final CreateMapFragment fragment = CreateMapFragment.newInstance();
        final FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.map_creator_frame_layout, fragment, MAP_CREATOR_FRAGMENT);
        fragmentTransaction.commit();


    }

    @Override
    public void onClick(View v) {
        RestClient client = RetrofitInstance.getRestClient();

        client.createMap(map).enqueue(new Callback<Maps>() {
            @Override
            public void onResponse(Call<Maps> call, Response<Maps> response) {
                if (response.isSuccessful()) { // Response code from 200 to 30
                    Toast.makeText(getApplicationContext(), "Mapa creado correctamente", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    APIError error = ErrorUtils.parseError(response);
                    String errorMessage = error.getMessage();
                    Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Maps> call, Throwable t) {
                Toast.makeText(getApplicationContext(), getString(R.string.connection_failure), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
