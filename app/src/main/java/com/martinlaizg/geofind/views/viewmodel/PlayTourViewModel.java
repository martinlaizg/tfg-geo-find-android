package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;
import com.martinlaizg.geofind.data.access.api.service.exceptions.APIException;
import com.martinlaizg.geofind.data.access.database.entities.Place;
import com.martinlaizg.geofind.data.access.database.entities.Play;
import com.martinlaizg.geofind.data.access.database.entities.Tour;
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
		int numPlaces = play.getPlaces().size();
		if(numPlaces >= play.getTour().getPlaces().size()) {
			setError(new APIException(ErrorType.COMPLETED));
			return null;
		}
		return play.getTour().getPlaces().get(numPlaces);
	}

	public APIException getError() {
		return error;
	}

	public void setError(APIException error) {
		this.error = error;
	}

	public Play getPlay() {
		return play;
	}

	public MutableLiveData<Boolean> completePlace(Integer place_id) {
		MutableLiveData<Boolean> c = new MutableLiveData<>();
		new Thread(() -> {
			boolean b = false;
			try {
				play = playRepo.completePlay(play.getId(), place_id);
				b = true;
			} catch(APIException e) {
				setError(e);
			}
			c.postValue(b);
		}).start();
		return c;
	}

	public Tour getTour() {
		return play.getTour();
	}

}
