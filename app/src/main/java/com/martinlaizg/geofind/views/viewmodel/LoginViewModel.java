package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.Crypto;
import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository userRepo;
	private Login login;
	private APIException error;

	public LoginViewModel(Application application) {
		super(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	public MutableLiveData<User> login() {
		MutableLiveData<User> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				User user = userRepo.login(login);
				u.postValue(user);
			} catch(APIException e) {
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
				User user = userRepo.registry(login);
				u.postValue(user);
			} catch(APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public void setLogin(String email, String password, Login.LoginType loginType) {
		login = new Login(email, Crypto.hash(password), loginType);
	}

	public void setRegistry(String name, String username, String email, String password,
			Login.LoginType loginType) {
		login = new Login(name, email, username, Crypto.hash(password), loginType);
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}
}
