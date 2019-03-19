package com.martinlaizg.geofind.views.fragment.play;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.martinlaizg.geofind.R;

import androidx.fragment.app.Fragment;
import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayCompassFragment extends Fragment {


    @BindView(R.id.play_compass)
    ImageView play_compass;


    public PlayCompassFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_compass, container, false);
        ButterKnife.bind(this, view);
        return view;
    }


}
