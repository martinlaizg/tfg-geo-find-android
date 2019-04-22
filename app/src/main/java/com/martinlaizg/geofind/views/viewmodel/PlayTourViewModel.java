package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.repository.PlayRepository;

public class PlayTourViewModel
		extends AndroidViewModel {

	private final PlayRepository playRepo;

	private APIException error;

	public PlayTourViewModel(@NonNull Application application) {
		super(application);
		playRepo = new PlayRepository(application);
	}

	public MutableLiveData<Play> loadPlay(int tour_id, int user_id) {
		MutableLiveData<Play> m = new MutableLiveData<>();
		new Thread(() -> {
			Play p = playRepo.getPlay(tour_id, user_id);
			m.postValue(p);
		}).start();
		return m;
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

	public Place getNextPlace() {
		return null;
	}
}
