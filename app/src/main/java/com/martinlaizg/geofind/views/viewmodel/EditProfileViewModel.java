package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.entities.Login;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.User;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;

public class EditProfileViewModel
		extends AndroidViewModel {

	private final UserRepository userRepo;
	private APIException error;

	public EditProfileViewModel(Application application) {
		super(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	public MutableLiveData<User> updateUser(Login login, User user, boolean isChangePassword) {
		MutableLiveData<User> u = new MutableLiveData<>();
		new Thread(() -> {
			try {
				u.postValue(userRepo.updateUser(login, user, isChangePassword));
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

	public void setError(APIException error) {
		this.error = error;
	}

}
