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

	UserRepository(Application application) {
		AppDatabase database = AppDatabase.getInstance(application);
		userDAO = database.userDAO();
		userService = UserService.getInstance();
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public User login(User u) throws APIException {
		u = userService.login(u);
		if(u != null) {
			userDAO.insert(u);
		}
		return u;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public User registry(User u) throws APIException {
		u = userService.registry(u);
		if(u != null) {
			userDAO.insert(u);
		}
		return u;
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public void insert(User user) {
		if(user != null) {
			userDAO.insert(user);
		}
	}

	/**
	 * TODO
	 *
	 * @return
	 */
	public User getUser(int user_id) {
		return userDAO.getUser(user_id);
	}

	/**
	 * Send message to support
	 *
	 * @param title   the title
	 * @param message the message
	 * @return the response
	 * @throws APIException the exception
	 */
	public boolean sendMessage(String title, String message) throws APIException {
		return userService.sendMessage(title, message);
	}
}
