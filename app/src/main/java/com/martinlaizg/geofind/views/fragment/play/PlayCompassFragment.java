package com.martinlaizg.geofind.views.fragment.play;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.martinlaizg.geofind.R;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.content.Context.SENSOR_SERVICE;

public class PlayCompassFragment
		extends PlayTourFragment
		implements SensorEventListener {

	private static final String TAG = PlayCompassFragment.class.getSimpleName();
	private final float[] mLastAccelerometer = new float[3];
	private final float[] mLastMagnetometer = new float[3];
	private final float[] mR = new float[9];
	private final float[] mOrientation = new float[3];
	@BindView(R.id.navigation_image)
	ImageView navigation_image;
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Sensor mMagnetometer;
	private boolean mLastAccelerometerSet = false;
	private boolean mLastMagnetometerSet = false;
	private float mCurrentDegree = 0f;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_compass, container, false);
		ButterKnife.bind(this, view);
		mSensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mMagnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		return view;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(event.sensor == mAccelerometer) {
			System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
			mLastAccelerometerSet = true;
		} else if(event.sensor == mMagnetometer) {
			System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
			mLastMagnetometerSet = true;
		}
		if(mLastAccelerometerSet && mLastMagnetometerSet) {
			SensorManager.getRotationMatrix(mR, null, mLastAccelerometer, mLastMagnetometer);
			SensorManager.getOrientation(mR, mOrientation);
			float azimuthInRadians = mOrientation[0];
			float azimuthInDegress = (float) (Math.toDegrees(azimuthInRadians) + 360) % 360;
			RotateAnimation ra = new RotateAnimation(mCurrentDegree, -azimuthInDegress,
			                                         Animation.RELATIVE_TO_SELF, 0.5f,
			                                         Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(250);
			ra.setFillAfter(true);
			navigation_image.startAnimation(ra);
			mCurrentDegree = -azimuthInDegress;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected String TAG() {
		return TAG;
	}

	@Override
	public void onResume() {
		super.onResume();
		super.onResume();
		mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_GAME);
		mSensorManager.registerListener(this, mMagnetometer, SensorManager.SENSOR_DELAY_GAME);
	}

	@Override
	public void onPause() {
		super.onPause();
		mSensorManager.unregisterListener(this, mAccelerometer);
		mSensorManager.unregisterListener(this, mMagnetometer);
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
