package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.User;
import com.martinlaizg.geofind.data.access.database.repository.UserRepository;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository repository;

	private String email;
	private String password;

	public LoginViewModel(Application application) {
		super(application);
		repository = new UserRepository(application);
		email = "";
		password = "";

	}

	public MutableLiveData<User> login() {
		return repository.login(email, password);
	}

	public void setLogin(String email, String password) {
		this.email = email;
		this.password = password;
	}
}
