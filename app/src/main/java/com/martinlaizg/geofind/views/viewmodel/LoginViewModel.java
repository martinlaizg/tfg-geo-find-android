package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.UserRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository repository;

	private User user;
	private APIException error;

	public LoginViewModel(Application application) {
		super(application);
		repository = new UserRepository(application);
		user = new User();
	}

	public MutableLiveData<User> login() {
		MutableLiveData<User> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				user = repository.login(user);
				u.postValue(user);
			} catch (APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public MutableLiveData<User> registry() {

		MutableLiveData<User> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				user = repository.registry(user);
				u.postValue(user);
			} catch (APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public void setLogin(String email, String password) {
		user.setEmail(email);
		user.setPassword(password);
	}


	public void setRegistry(String name, String username, String email, String password) {
		user.setName(name);
		user.setUsername(username);
		user.setEmail(email);
		user.setPassword(password);
	}

	public String getEmail() {
		return user.getEmail();
	}

	public void setEmail(String email) {
		user.setEmail(email);
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
