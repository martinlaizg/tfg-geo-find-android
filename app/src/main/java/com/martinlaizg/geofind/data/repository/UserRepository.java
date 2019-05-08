package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.UserService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entities.User;

public class UserRepository {

	private final UserDAO userDAO;
	private final UserService userService;

	public UserRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		userDAO = database.userDAO();
		userService = UserService.getInstance();
	}

	public User login(User u) throws APIException {
		u = userService.login(u);
		if(u != null) {
			userDAO.insert(u);
		}
		return u;
	}

	public User registry(User u) throws APIException {
		u = userService.registry(u);
		if(u != null) {
			userDAO.insert(u);
		}
		return u;
	}

	public void insert(User user) {
		if(user != null) {
			userDAO.insert(user);
		}
	}

	public User getUser(int user_id) {
		return userDAO.getUser(user_id);
	}
}
