package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.service.UserService;

import androidx.lifecycle.MutableLiveData;

public class UserRepository {

    private UserDAO userDAO;
    private UserService userService;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDAO = database.userDAO();
        userService = UserService.getInstance();
    }


    public MutableLiveData<User> login(String email, String password) {
        MutableLiveData<User> user = new MutableLiveData<>();
        new Thread(new Runnable() {
            @Override
            public void run() {
                User u = new User(email, password);
                u = userService.login(u);
                if (u != null && !u.getEmail().isEmpty()) {
                    userDAO.insert(u);
                    user.postValue(u);
                }

            }
        }).start();
        return user;
    }
}
