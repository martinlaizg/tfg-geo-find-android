package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.repository.PlayRepository;

import java.util.ArrayList;
import java.util.List;

public class PlayTourViewModel
		extends AndroidViewModel {

	private final PlayRepository playRepo;

	private ErrorType error;
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
				setError(e.getType());
			}
		}).start();
		return m;
	}

	private Place getNextPlace() {
		List<Place> places = new ArrayList<>(play.getTour().getPlaces());
		for(int i = 0; i < places.size(); i++) {
			for(Place p : play.getPlaces()) {
				if(places.get(i).getId() == p.getId()) {
					places.remove(i);
					i--;
					break;
				}
			}
		}
		if(places.size() == 0) return null;
		return places.get(0);
	}

	public ErrorType getError() {
		return error != null ?
				error :
				ErrorType.OTHER;
	}

	private void setError(ErrorType error) {
		this.error = error;
	}

	public Play getPlay() {
		return play;
	}

	public MutableLiveData<Place> completePlace(Integer place_id) {
		MutableLiveData<Place> c = new MutableLiveData<>();
		new Thread(() -> {
			try {
				if(play.getId() == 0) {
					play = playRepo.createPlay(play.getUser_id(), play.getUser_id());
				}
				play = playRepo.completePlace(play.getId(), place_id);
				c.postValue(getNextPlace());
			} catch(APIException e) {
				setError(e.getType());
				c.postValue(null);
			}
		}).start();
		return c;
	}

	public boolean tourIsCompleted() {
		return play.getPlaces().size() == play.getTour().getPlaces().size();
	}
}
