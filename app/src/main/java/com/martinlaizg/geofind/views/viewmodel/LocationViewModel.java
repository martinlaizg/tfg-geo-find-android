package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entities.Place;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class LocationViewModel
		extends AndroidViewModel {


	public LocationViewModel(@NonNull Application application) {
		super(application);
	}


	public MutableLiveData<List<Place>> getAllLocations() {
		// TODO
		return new MutableLiveData<>();
	}
}
