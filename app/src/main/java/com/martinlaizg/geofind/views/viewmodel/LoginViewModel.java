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
	private String name;

	public LoginViewModel(Application application) {
		super(application);
		repository = new UserRepository(application);
		name = "";
		email = "";
		password = "";
	}

	public MutableLiveData<User> login() {
		return repository.login(email, password);
	}

	public MutableLiveData<User> registry() {
		return repository.registry(name, email, password);
	}

	public void setLogin(String email, String password) {
		this.email = email;
	}


	public void setRegistry(String name, String email, String password) {
		this.name = name;
		this.email = email;
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
