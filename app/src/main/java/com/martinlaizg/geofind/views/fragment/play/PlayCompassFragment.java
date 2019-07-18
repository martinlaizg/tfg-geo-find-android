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
	private static final float MIN_ROTATION = 8f;

	private final float[] accData = new float[3];
	private final float[] magnetData = new float[3];
	private final float[] rotationMatrix = new float[9];
	private final float[] mOrientation = new float[3];

	@BindView(R.id.navigation_image)
	ImageView navigation_image;

	private SensorManager sensorManager;
	private Sensor accelSensor;
	private Sensor magnetSensor;
	// Degree from north to destination 0 -> 360
	private float bearing;
	private float imgRotation;

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
			@Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_play_compass, container, false);
		ButterKnife.bind(this, view);
		sensorManager = (SensorManager) requireActivity().getSystemService(SENSOR_SERVICE);
		accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		magnetSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		return view;
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
		if(usrLocation != null && placeLocation != null) {

			// Copy the sensor data
			if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
				System.arraycopy(event.values, 0, accData, 0, event.values.length);
			} else if(event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(event.values, 0, magnetData, 0, event.values.length);
			}

			// Calculate the rotation matrix
			SensorManager.getRotationMatrix(rotationMatrix, null, accData, magnetData);
			// Calculate the orientation
			SensorManager.getOrientation(rotationMatrix, mOrientation);

			// Get user orientation
			float userOrientation = (float) Math.toDegrees(mOrientation[0]);
			if(userOrientation < 0) userOrientation = 360 + userOrientation;

			// Get orientation
			float direction = bearing - userOrientation;
			if(direction < 0) direction = 360 + direction;

			Log.i(TAG, " direction " + direction + "  \timgRot " + imgRotation);

			if(Math.abs(imgRotation - direction) < 180) {
				// Smooth the movement
				float alpha = 0.95f;
				direction = imgRotation * alpha + direction * (1 - alpha);
			}

			RotateAnimation ra = new RotateAnimation(imgRotation, direction,
			                                         Animation.RELATIVE_TO_SELF, 0.5f,
			                                         Animation.RELATIVE_TO_SELF, 0.5f);
			ra.setDuration(100);
			// set the animation after the end of the reservation status
			ra.setFillAfter(false);
			// Start the animation
			navigation_image.startAnimation(ra);
			imgRotation = direction;
		}
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {
		// Do nothing
	}

	@Override
	protected String TAG() {
		return TAG;
	}

	@Override
	public void onResume() {
		super.onResume();
		sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_NORMAL);
		sensorManager.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		super.onPause();
		sensorManager.unregisterListener(this, accelSensor);
		sensorManager.unregisterListener(this, magnetSensor);
	}

	@Override
	void updateView() {
		if(usrLocation != null && placeLocation != null) {
			bearing = usrLocation.bearingTo(placeLocation);
			if(bearing < 0) bearing = 360 + bearing;
			//			declination = new GeomagneticField((float) usrLocation.getLatitude(),
			//			                                   (float) usrLocation.getLongitude(),
			//			                                   (float) usrLocation.getAltitude(),
			//			                                   System.currentTimeMillis()).getDeclination();
		}
	}
}
