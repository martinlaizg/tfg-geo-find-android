package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.UserRepository;

public class LoginViewModel
		extends AndroidViewModel {

	private final UserRepository userRepo;
	private ErrorType error;

	public LoginViewModel(Application application) {
		super(application);
		userRepo = UserRepository.getInstance(application);
	}

	public MutableLiveData<User> login(Login login) {
		MutableLiveData<User> t = new MutableLiveData<>();
		new Thread(() -> {
			try {
				User user = userRepo.login(login);
				t.postValue(user);
			} catch(APIException e) {
				setError(e.getType());
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
				setError(e.getType());
				u.postValue(null);
			}
		}).start();
		return u;
	}

	public ErrorType getError() {
		return error;
	}

	private void setError(ErrorType error) {
		this.error = error;
	}

}
