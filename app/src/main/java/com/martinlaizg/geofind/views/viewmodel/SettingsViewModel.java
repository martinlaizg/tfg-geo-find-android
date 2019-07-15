package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;
import com.martinlaizg.geofind.data.repository.UserRepository;

public class SettingsViewModel
		extends AndroidViewModel {

	private static final String TAG = SettingsViewModel.class.getSimpleName();
	private final UserRepository userRepo;

	private APIException error;

	public SettingsViewModel(Application application) {
		super(application);
		userRepo = RepositoryFactory.getUserRepository(application);
	}

	public APIException getError() {
		return error;
	}

	public MutableLiveData<Boolean> sendMessage(String title, String message) {
		MutableLiveData<Boolean> r = new MutableLiveData<>();
		new Thread(() -> {
			try {
				userRepo.sendMessage(title, message);
				r.postValue(true);
			} catch(APIException e) {
				Log.e(TAG, "sendMessage: ", e);
				setError(e);
				r.postValue(null);
			}
		}).start();
		return r;
	}

	private void setError(APIException error) {
		this.error = error;
	}
}
