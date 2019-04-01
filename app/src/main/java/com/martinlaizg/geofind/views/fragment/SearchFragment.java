package com.martinlaizg.geofind.views.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;

import org.jetbrains.annotations.NotNull;

import androidx.fragment.app.Fragment;

public class SearchFragment
		extends Fragment {


	public SearchFragment() {
		// Required empty public constructor
	}

	@Override
	public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_search, container, false);
	}

}
