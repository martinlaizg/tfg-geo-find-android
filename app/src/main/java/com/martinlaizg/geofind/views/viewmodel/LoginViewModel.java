package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.UserEntity;
import com.martinlaizg.geofind.data.repository.UserRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository repository;

	private UserEntity userEntity;
	private APIException error;

	public LoginViewModel(Application application) {
		super(application);
		repository = new UserRepository(application);
		userEntity = new UserEntity();
	}

	public MutableLiveData<UserEntity> login() {
		MutableLiveData<UserEntity> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				userEntity = repository.login(userEntity);
				u.postValue(userEntity);
			} catch (APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public MutableLiveData<UserEntity> registry() {

		MutableLiveData<UserEntity> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				userEntity = repository.registry(userEntity);
				u.postValue(userEntity);
			} catch (APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public void setLogin(String email, String password) {
		userEntity.setEmail(email);
		userEntity.setPassword(password);
	}


	public void setRegistry(String name, String username, String email, String password) {
		userEntity.setName(name);
		userEntity.setUsername(username);
		userEntity.setEmail(email);
		userEntity.setPassword(password);
	}

	public String getEmail() {
		return userEntity.getEmail();
	}

	public void setEmail(String email) {
		userEntity.setEmail(email);
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
