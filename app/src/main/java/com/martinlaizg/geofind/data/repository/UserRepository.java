package com.martinlaizg.geofind.data.repository;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.entities.Login;
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
	 * Login a user
	 *
	 * @param login
	 * 		login data to login (email and password only)
	 * @return the logged user
	 * @throws APIException
	 * 		exception from API
	 */
	public User login(Login login) throws APIException {
		User user = userService.login(login);
		if(user != null) {
			userDAO.insert(user);
		}
		return user;
	}

	/**
	 * Registry a user
	 *
	 * @param login
	 * 		login data to be registered (email, username and password only)
	 * @return the user registered
	 * @throws APIException
	 * 		exception from API
	 */
	public User registry(Login login) throws APIException {
		User user = userService.registry(login);
		if(user != null) {
			userDAO.insert(user);
		}
		return user;
	}

	/**
	 * Insert a user into the database
	 *
	 * @param user
	 * 		user to be inserted
	 */
	public void insert(User user) {
		if(user != null) {
			User u = userDAO.getUser(user.getId());
			if(u == null) {
				userDAO.insert(user);
			} else {
				userDAO.update(user);
			}
		}
	}

	/**
	 * Get a user from database
	 *
	 * @param user_id
	 * 		id of the user
	 * @return the user
	 */
	public User getUser(int user_id) {
		return userDAO.getUser(user_id);
	}

	/**
	 * Send message to support
	 *
	 * @param title
	 * 		the title
	 * @param message
	 * 		the message
	 * @return the response
	 * @throws APIException
	 * 		the exception
	 */
	public boolean sendMessage(String title, String message) throws APIException {
		return userService.sendMessage(title, message);
	}

	/**
	 * Update the user on the server
	 *
	 * @param login
	 * 		the user login
	 * @param user
	 * 		the new user data
	 * @return the new User
	 * @throws APIException
	 * 		the API exception
	 */
	public User updateUser(Login login, User user) throws APIException {
		User u = userService.update(login, user);
		userDAO.insert(u);
		return u;
	}
}
