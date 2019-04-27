package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.repository.PlayRepository;
import com.martinlaizg.geofind.data.repository.RepositoryFactory;

public class PlayTourViewModel
		extends AndroidViewModel {

	private final PlayRepository playRepo;

	private APIException error;
	private Play play;

	public PlayTourViewModel(@NonNull Application application) {
		super(application);
		playRepo = RepositoryFactory.getPlayRepository(application);
	}

	public MutableLiveData<Play> loadPlay(int user_id, int tour_id) {
		MutableLiveData<Play> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				play = playRepo.getPlay(user_id, tour_id);
			} catch(APIException e) {
				setError(e);
			}
			m.postValue(play);
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
		int numPlaces = play.getPlaces().size();
		if(numPlaces >= play.getTour().getPlaces().size()) {
			return null;
		}
		return play.getTour().getPlaces().get(numPlaces);
	}
}
