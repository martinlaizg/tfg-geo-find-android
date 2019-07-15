package com.martinlaizg.geofind.views.fragment.play;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import com.martinlaizg.geofind.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayThermFragment
		extends PlayTourFragment {

	private static final String TAG = PlayThermFragment.class.getSimpleName();

	@BindView(R.id.therm_card)
	CardView therm_card;

	private float lastDistance = 0;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_therm, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	protected String TAG() {
		return TAG;
	}

	@Override
	void updateView() {
		if(distance > lastDistance) {
			therm_card.setCardBackgroundColor(getResources().getColor(R.color.red, null));
		} else {
			therm_card.setCardBackgroundColor(getResources().getColor(R.color.green, null));
		}
		lastDistance = distance;
	}
}
