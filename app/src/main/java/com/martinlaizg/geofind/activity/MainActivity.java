package com.martinlaizg.geofind.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.martinlaizg.geofind.R;
import com.martinlaizg.geofind.adapter.UserAdapter;
import com.martinlaizg.geofind.client.RestClient;
import com.martinlaizg.geofind.controller.RetrofitInstance;
import com.martinlaizg.geofind.entity.User;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    // Display items
    private Button btn_load;
    private RecyclerView rv_list_users;

    // Other
    private UserAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        btn_load = findViewById(R.id.btn_load);
        rv_list_users = findViewById(R.id.rv_list_users);


        setListeners();
    }

    public void setListeners(){
        btn_load.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestClient restClient = RetrofitInstance.getRetrofitInstance().create(RestClient.class);
                Call<List<User>> call = restClient.getUsers();
                Log.d("URL", call.request().url().toString());
                call.enqueue(new Callback<List<User>>() {
                    @Override
                    public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                        generateUsersList(response.body());
                    }

                    @Override
                    public void onFailure(Call<List<User>> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Something went wrong...Error message: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    private void generateUsersList(List<User> users){
        // Get the RecyclerView
        rv_list_users = findViewById(R.id.rv_list_users);
        // Create de adapter
        adapter = new UserAdapter(users);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        rv_list_users.setLayoutManager(layoutManager);
        rv_list_users.setAdapter(adapter);

    }

}
