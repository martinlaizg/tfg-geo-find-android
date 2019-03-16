package com.martinlaizg.geofind.views.fragment.play;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;

import androidx.fragment.app.Fragment;


public class PlayThermFragment extends Fragment {


    public PlayThermFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_therm, container, false);
    }

}
