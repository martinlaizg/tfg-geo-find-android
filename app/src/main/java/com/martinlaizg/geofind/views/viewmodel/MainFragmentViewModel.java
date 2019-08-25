package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.repository.PlayRepository;

import java.util.List;

public class MainFragmentViewModel
		extends AndroidViewModel {

	private static final String TAG = MainFragmentViewModel.class.getSimpleName();

	private final PlayRepository playRepo;
	private ErrorType error;

	public MainFragmentViewModel(@NonNull Application application) {
		super(application);
		playRepo = PlayRepository.getInstance(application);
	}

	public ErrorType getError() {
		return error;
	}

	public MutableLiveData<List<Play>> getUserPlays(int user_id) {
		MutableLiveData<List<Play>> plays = new MutableLiveData<>();
		new Thread(() -> {
			try {
				plays.postValue(playRepo.getUserPlays(user_id));
			} catch(APIException e) {
				setError(e.getType());
				plays.postValue(null);
			}
		}).start();
		return plays;
	}

	private void setError(ErrorType error) {
		this.error = error;
	}
}
