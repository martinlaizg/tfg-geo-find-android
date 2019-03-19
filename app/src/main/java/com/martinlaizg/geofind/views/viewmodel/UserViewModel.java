package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.repository.UserRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public class UserViewModel extends AndroidViewModel {

    private UserRepository repository;


    public UserViewModel(@NonNull Application application) {
        super(application);
        repository = new UserRepository(application);
    }
}
