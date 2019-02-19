package com.martinlaizg.geofind.dataAccess.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.dataAccess.database.AppDatabase;
import com.martinlaizg.geofind.dataAccess.database.dao.UserDAO;
import com.martinlaizg.geofind.dataAccess.database.entity.User;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {
    private UserDAO userDAO;
    private LiveData<List<User>> allUsers;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDAO = database.userDAO();
        allUsers = userDAO.getAllUsers();
    }

    public void insert(User user) {

    }

    public void update(User user) {

    }

    public void delete(User user) {

    }

    public void deleteAllUsers() {

    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }
}
