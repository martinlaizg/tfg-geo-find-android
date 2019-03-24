package com.martinlaizg.geofind.views.viewmodel;

import android.app.Application;

import com.martinlaizg.geofind.data.access.database.entity.Map;
import com.martinlaizg.geofind.data.access.database.repository.MapRepository;
import com.martinlaizg.geofind.views.fragment.list.MapListFragment;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class MapListViewModel
		extends AndroidViewModel {

	MapListFragment fragment;

	private MapRepository repository;


	public MapListViewModel(@NonNull Application application) {
		super(application);
		repository = new MapRepository(application);
	}


	public MutableLiveData<List<Map>> getAllMaps() {
		return repository.getAllMaps();
	}

	public void refreshMaps() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				repository.refreshMaps();

			}
		}).start();
	}

}
