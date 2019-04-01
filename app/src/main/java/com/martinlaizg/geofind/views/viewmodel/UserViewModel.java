package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.repository.UserRepository;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

class UserViewModel
		extends AndroidViewModel {


	public UserViewModel(@NonNull Application application) {
		super(application);
		UserRepository repository = new UserRepository(application);
	}
}
