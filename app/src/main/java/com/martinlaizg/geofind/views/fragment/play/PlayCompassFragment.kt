package com.martinlaizg.geofind.views.fragment.play

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import android.widget.ImageView
import butterknife.BindView
import butterknife.ButterKnife
import com.martinlaizg.geofind.R

class PlayCompassFragment : PlayTourFragment(), SensorEventListener {
	private val accData = FloatArray(3)
	private val magnetData = FloatArray(3)
	private val rotationMatrix = FloatArray(9)
	private val mOrientation = FloatArray(3)

	@kotlin.jvm.JvmField
	@BindView(R.id.navigation_image)
	var navigation_image: ImageView? = null
	private var sensorManager: SensorManager? = null
	private var accelerationSensor: Sensor? = null
	private var magnetSensor: Sensor? = null

	// Degree from north to destination 0 -> 360
	private var bearing = 0f
	private var imgRotation = 0f
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_play_compass, container, false)
		ButterKnife.bind(this, view)
		sensorManager = requireActivity().getSystemService(Context.SENSOR_SERVICE) as SensorManager
		if (sensorManager != null) {
			accelerationSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
			magnetSensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)
		}
		return view
	}

	override fun onSensorChanged(event: SensorEvent) {
		if (usrLocation != null && placeLocation != null) {

			// Copy the sensor data
			if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
				System.arraycopy(event.values, 0, accData, 0, event.values.size)
			} else if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
				System.arraycopy(event.values, 0, magnetData, 0, event.values.size)
			}

			// Calculate the rotation matrix
			SensorManager.getRotationMatrix(rotationMatrix, null, accData, magnetData)
			// Calculate the orientation
			SensorManager.getOrientation(rotationMatrix, mOrientation)

			// Get user orientation
			var userOrientation = Math.toDegrees(mOrientation[0].toDouble()).toFloat()
			if (userOrientation < 0) userOrientation = 360 + userOrientation

			// Get orientation
			var direction = bearing - userOrientation
			if (direction < 0) direction = 360 + direction
			Log.i(TAG, " direction $direction  \timgRot $imgRotation")
			if (Math.abs(imgRotation - direction) < 180) {
				// Smooth the movement
				val alpha = 0.95f
				direction = imgRotation * alpha + direction * (1 - alpha)
			}
			val ra = RotateAnimation(imgRotation, direction,
					Animation.RELATIVE_TO_SELF, 0.5f,
					Animation.RELATIVE_TO_SELF, 0.5f)
			ra.duration = 100
			// set the animation after the end of the reservation status
			ra.fillAfter = false
			// Start the animation
			navigation_image!!.startAnimation(ra)
			imgRotation = direction
		}
	}

	override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
		// Do nothing
	}

	override fun getTag(): String {
		return TAG
	}

	override fun onResume() {
		super.onResume()
		sensorManager!!.registerListener(this, accelerationSensor, SensorManager.SENSOR_DELAY_NORMAL)
		sensorManager!!.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL)
	}

	override fun onPause() {
		super.onPause()
		sensorManager!!.unregisterListener(this, accelerationSensor)
		sensorManager!!.unregisterListener(this, magnetSensor)
	}

	public override fun updateView() {
		if (usrLocation != null && placeLocation != null) {
			bearing = usrLocation!!.bearingTo(placeLocation)
			if (bearing < 0) bearing = 360 + bearing
			//			declination = new GeomagneticField((float) usrLocation.getLatitude(),
			//			                                   (float) usrLocation.getLongitude(),
			//			                                   (float) usrLocation.getAltitude(),
			//			                                   System.currentTimeMillis()).getDeclination();
		}
	}

	companion object {
		private val TAG = PlayCompassFragment::class.java.simpleName
		private const val MIN_ROTATION = 8f
	}
}