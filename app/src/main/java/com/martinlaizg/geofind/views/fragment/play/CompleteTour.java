package com.martinlaizg.geofind.views.fragment.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.martinlaizg.geofind.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompleteTour
		extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_complete_tour, container, false);
	}

	public CompleteTour() {
		// Required empty public constructor
	}

}
