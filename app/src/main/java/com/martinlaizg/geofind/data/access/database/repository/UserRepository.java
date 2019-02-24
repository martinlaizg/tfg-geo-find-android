package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.service.UserService;

import java.util.List;

import androidx.lifecycle.LiveData;

public class UserRepository {


    private UserDAO userDAO;
    private LiveData<List<User>> allUsers;
    private String TAG = UserRepository.class.getSimpleName();
    private UserService userService;

    public UserRepository(Application application) {
        AppDatabase database = AppDatabase.getInstance(application);
        userDAO = database.userDAO();
        userService = UserService.getInstance();
        allUsers = userDAO.getAllUsers();
    }

    public void insert(User user) {
        new InsertUserAsyncTask(userDAO).execute(user);
    }

    public void update(User user) {
        new UpdateUserAsyncTask(userDAO).execute(user);
    }

    public void delete(User user) {
        new DeleteUserAsyncTask(userDAO).execute(user);
    }

    public void deleteAllUsers() {
        new DeleteAllUserAsyncTask(userDAO).execute();
    }

    public LiveData<List<User>> getAllUsers() {
        return allUsers;
    }

    public User login(String email, String password) {
        User userLogin = new User();
        userLogin.setEmail(email);
        userLogin.setPassword(password);
        try {
            return new LoginUserAsyncTask(userDAO, userService).execute(userLogin).get();
        } catch (Exception e) {
            Log.e(TAG, "login: ", e);
        }
        return null;
    }

    private static class InsertUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private InsertUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.insert(users[0]);
            return null;
        }
    }

    private static class UpdateUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private UpdateUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.update(users[0]);
            return null;
        }
    }

    private static class DeleteUserAsyncTask extends AsyncTask<User, Void, Void> {
        private UserDAO userDAO;

        private DeleteUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(User... users) {
            userDAO.delete(users[0]);
            return null;
        }
    }

    private static class DeleteAllUserAsyncTask extends AsyncTask<Void, Void, Void> {
        private UserDAO userDAO;

        private DeleteAllUserAsyncTask(UserDAO userDAO) {
            this.userDAO = userDAO;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            userDAO.deleteAllUsers();
            return null;
        }
    }

    private static class LoginUserAsyncTask extends AsyncTask<User, Void, User> {
        private final UserDAO userDao;
        private final UserService userService;

        private LoginUserAsyncTask(UserDAO userDAO, UserService userService) {
            userDao = userDAO;
            this.userService = userService;
        }

        @Override
        protected User doInBackground(User... users) {
            return userService.login(users[0]);
        }
    }
}
