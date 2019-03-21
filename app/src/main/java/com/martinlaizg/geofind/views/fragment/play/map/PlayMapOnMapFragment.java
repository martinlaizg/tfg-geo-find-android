package com.martinlaizg.geofind.views.fragment.play;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.martinlaizg.geofind.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMapOnMapFragment extends Fragment {


    public PlayMapOnMapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_map_on_map, container, false);
    }

}
