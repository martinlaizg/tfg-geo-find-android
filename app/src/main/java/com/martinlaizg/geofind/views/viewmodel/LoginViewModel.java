package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.database.repository.UserRepository;

import androidx.lifecycle.AndroidViewModel;

public class LoginViewModel extends AndroidViewModel {

    UserRepository repository;

    public LoginViewModel(Application application) {
        super(application);
        repository = new UserRepository(application);

    }

    public User login(String email, String password) {
        return repository.login(email, password);
    }

}
