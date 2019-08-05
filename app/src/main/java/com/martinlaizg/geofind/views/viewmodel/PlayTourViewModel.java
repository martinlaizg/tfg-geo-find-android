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
	private Play play;

	public PlayTourViewModel(@NonNull Application application) {
		super(application);
		playRepo = PlayRepository.getInstance(application);
	}

	public MutableLiveData<Place> loadPlay(int user_id, int tour_id) {
		MutableLiveData<Place> m = new MutableLiveData<>();
		new Thread(() -> {
			try {
				play = playRepo.getPlay(user_id, tour_id);
				if(play == null) {
					play = playRepo.createPlay(user_id, tour_id);
				}
				m.postValue(getNextPlace());
			} catch(APIException e) {
				setError(e);
			}
		}).start();
		return m;
	}

	public Place getNextPlace() {
		int numCompletedPlaces = play.getPlaces().size();
		if(numCompletedPlaces >= play.getTour().getPlaces().size()) return null;
		return play.getTour().getPlaces().get(numCompletedPlaces);
	}

	public APIException getError() {
		return error;
	}

	private void setError(APIException error) {
		this.error = error;
	}

	public Play getPlay() {
		return play;
	}

	public MutableLiveData<Place> completePlace(Integer place_id) {
		MutableLiveData<Place> c = new MutableLiveData<>();
		new Thread(() -> {
			try {
				play = playRepo.completePlace(play.getId(), place_id);
				c.postValue(getNextPlace());
			} catch(APIException e) {
				setError(e);
				c.postValue(null);
			}
		}).start();
		return c;
	}

	public boolean tourIsCompleted() {
		return play.getPlaces().size() == play.getTour().getPlaces().size();
	}
}
