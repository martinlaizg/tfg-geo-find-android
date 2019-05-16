package com.martinlaizg.geofind.views.fragment.play;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.martinlaizg.geofind.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayCompassFragment
		extends PlayTourFragment {

	private static final String TAG = PlayCompassFragment.class.getSimpleName();

	@BindView(R.id.navigation_image)
	ImageView navigation_image;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_compass, container, false);
		ButterKnife.bind(this, view);
		return view;
	}

	@Override
	protected String TAG() {
		return TAG;
	}

	@Override
	void updateView() {
		if(usrLocation != null && placeLocation != null) {
			// Rotate image
			float bearing = usrLocation.bearingTo(placeLocation);
			Log.d(TAG, "updateView: rotate image=" + bearing);
			navigation_image.setRotation(bearing);
		}
	}
}
