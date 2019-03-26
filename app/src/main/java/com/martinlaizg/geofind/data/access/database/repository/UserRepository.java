package com.martinlaizg.geofind.data.access.database.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.retrofit.service.UserService;

import androidx.lifecycle.MutableLiveData;

public class UserRepository {

	private static final String TAG = UserRepository.class.getSimpleName();
	private final UserDAO userDAO;
	private final UserService userService;

	public UserRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		userDAO = database.userDAO();
		userService = UserService.getInstance();
	}


	public MutableLiveData<User> login(String email, String password) {
		MutableLiveData<User> user = new MutableLiveData<>();
		new Thread(() -> {
			User u = userService.login(new User(email, password));
			if (u != null) {
				user.postValue(u);
				userDAO.insert(u);
			}
		}).start();
		return user;
	}
}
