package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.UserService;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.AppDatabase;
import com.martinlaizg.geofind.data.access.database.dao.UserDAO;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;

public class UserRepository {

	private static final String TAG = UserRepository.class.getSimpleName();
	private final UserDAO userDAO;
	private final UserService userService;

	public UserRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		userDAO = database.userDAO();
		userService = UserService.getInstance();
	}


	public UserEntity login(UserEntity u) throws APIException {
		u = userService.login(u);
		if (u != null) {
			userDAO.insert(u);
		}
		return u;
	}

	public UserEntity registry(UserEntity u) throws APIException {
		u = userService.registry(u);
		if (u != null) {
			userDAO.insert(u);
		}
		return u;
	}
}
