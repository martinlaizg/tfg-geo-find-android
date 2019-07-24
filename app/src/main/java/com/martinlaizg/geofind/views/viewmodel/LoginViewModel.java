package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository userRepo;
	private APIException error;

	public LoginViewModel(Application application) {
		super(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	public MutableLiveData<User> login(Login login) {
		MutableLiveData<User> t = new MutableLiveData<>();
		new Thread(() -> {
			try {
				User user = userRepo.login(login);
				t.postValue(user);
			} catch(APIException e) {
				setError(e);
				t.postValue(null);
			}
		}).start();
		return t;
	}

	public MutableLiveData<User> registry(Login registry) {
		MutableLiveData<User> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				User user = userRepo.registry(registry);
				u.postValue(user);
			} catch(APIException e) {
				setError(e);
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}

}
