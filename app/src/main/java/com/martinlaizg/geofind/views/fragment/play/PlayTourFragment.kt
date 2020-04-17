package com.martinlaizg.geofind.views.fragment.play

import android.Manifest.permission
import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import butterknife.BindView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel
import java.util.*

abstract class PlayTourFragment : Fragment(), LocationListener {
	@kotlin.jvm.JvmField
	@BindView(R.id.place_name)
	var place_name: TextView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.place_description)
	var place_description: TextView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.place_complete)
	var place_complete: TextView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.place_distance)
	var place_distance: TextView? = null
	var place: Place? = null
	var usrLocation: Location? = null
	var placeLocation: Location? = null
	var distance = Float.MAX_VALUE
	private val lock = false
	private var viewModel: PlayTourViewModel? = null
	private var locationManager: LocationManager? = null
	private var questionDialog: AlertDialog? = null
	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
	                                        grantResults: IntArray) {
		if (requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION && permissions.size >= 2) {
			if (permissions[0] == permission.ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1] == permission.ACCESS_FINE_LOCATION && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				Log.d(TAG(), "onRequestPermissionsResult: success")
				setPlace(place)
				return
			}
			Log.d(TAG(), "onRequestPermissionsResult: deny")
			requestPermissions(arrayOf(permission.ACCESS_COARSE_LOCATION,
					permission.ACCESS_FINE_LOCATION),
					PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(requireActivity()).get(PlayTourViewModel::class.java)
		val b = arguments
		var tour_id = 0
		if (b != null) {
			tour_id = b.getInt(TOUR_ID)
		}
		val u = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()))
		viewModel!!.loadPlay(u.id, tour_id).observe(this, Observer { place: Place? ->
			if (place == null) {
				Log.e(TAG(), "onViewCreated: Something went wrong")
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack(R.id.navTour, false)
				return@observe
			}
			setPlace(place)
		})
	}

	override fun onResume() {
		super.onResume()
		locationManager = requireActivity()
				.getSystemService(Context.LOCATION_SERVICE) as LocationManager
		requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		requestLocationUpdates()
	}

	override fun onPause() {
		super.onPause()
		removeLocationUpdates()
		requireActivity().window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
	}

	private fun removeLocationUpdates() {
		Log.i(TAG(), "remove location updates")
		locationManager!!.removeUpdates(this)
	}

	protected abstract fun getTag(): String
	private fun setPlace(nextPlace: Place?) {
		Log.d(TAG(), "setPlace: ")
		distance = Float.MAX_VALUE
		requestLocationUpdates()
		place = nextPlace
		placeLocation = place.getLocation()
		place_name!!.text = place!!.name
		place_description!!.text = place!!.description
		val numCompletedPlaces = viewModel.getPlay().places.size + 1
		val numPlaces = viewModel.getPlay().tour.places.size
		place_complete!!.text = resources.getQuantityString(R.plurals.place_number_number,
				numCompletedPlaces,
				numCompletedPlaces, numPlaces)
		updateView()
	}

	private fun requestLocationUpdates() {
		Log.d(TAG(), "request location updates")
		if (requireActivity().checkSelfPermission(permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(permission.ACCESS_COARSE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			requestPermissions(arrayOf(permission.ACCESS_COARSE_LOCATION,
					permission.ACCESS_FINE_LOCATION),
					PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION)
			return
		}
		usrLocation = locationManager!!.getLastKnownLocation(LocationManager.GPS_PROVIDER)
		locationManager
				.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ, LOC_DIST_REQ,
						this)
	}

	abstract fun updateView()

	@SuppressLint("MissingPermission")
	override fun onLocationChanged(location: Location) {
		Log.d(TAG(), "onLocationChanged: ")
		usrLocation = location

		// Set distance
		if (placeLocation != null) {
			distance = usrLocation!!.distanceTo(placeLocation) as Int.toFloat()
			if (distance > 1000f) {
				var newDistance = distance / 1000
				newDistance = Math.round(newDistance * 100f) / 100f
				place_distance
						.setText(resources.getString(R.string.place_distance_km, newDistance))
				Log.d(TAG(), "updateView: distance=" + newDistance + "km")
			} else {
				place_distance!!.text = resources.getString(R.string.place_distance_m, distance.toInt())
				Log.d(TAG(), "updateView: distance=" + distance + "m")
			}
			if (distance < DISTANCE_TO_COMPLETE) {
				Log.d(TAG(), "updateView: user arrive to the place")
				removeLocationUpdates()
				showCompleteDialog(place)
				return
			}
		}
		updateView()
	}

	override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {
		Log.d(TAG(), "onStatusChanged: ")
	}

	override fun onProviderEnabled(provider: String) {
		Log.d(TAG(), "onProviderEnabled: ")
	}

	override fun onProviderDisabled(provider: String) {
		Log.d(TAG(), "onProviderDisabled: ")
	}

	private fun completePlace() {
		Log.d(TAG(), "completePlace: ")
		if (questionDialog != null && questionDialog!!.isShowing) questionDialog!!.dismiss()
		viewModel!!.completePlace(place.getId()).observe(this, Observer { place: Place? ->
			if (place == null) {
				if (viewModel!!.tourIsCompleted()) {
					completeTour()
					return@observe
				}
				val error = viewModel.getError()
				error!!.showToast(requireContext())
			} else {
				Log.d(TAG(), "updateView: Place completed")
				setPlace(place)
			}
		})
	}

	private fun completeTour() {
		Log.d(TAG(), "completeTour: ")
		MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.tour_completed) //
				.setPositiveButton(R.string.ok) { dialog: DialogInterface, which: Int -> dialog.dismiss() }
				.setOnDismissListener { dialogInterface: DialogInterface? ->
					Navigation
							.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack(R.id.navTour, false)
				}.show()
	}

	private fun showCompleteDialog(place: Place?) {
		Log.d(TAG(), "showCompleteDialog: show dialog")
		val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
		if (place != null && place.question != null && !place.question.isEmpty()) {
			// Inflate view
			val dialogView = layoutInflater
					.inflate(R.layout.question_layout, ConstraintLayout(requireContext()),
							false)
			// Set question
			val question = dialogView.findViewById<TextView>(R.id.question)
			question.text = place.question
			val texts: List<MaterialButton> = Arrays.asList(dialogView.findViewById(R.id.answer1),
					dialogView.findViewById(R.id.answer2),
					dialogView.findViewById(R.id.answer3))
			// Set answers
			// Get random position to start
			var i = Random().nextInt(texts.size)

			// Set correct answer
			texts[i].text = place.answer
			texts[i].setOnClickListener { v: View? -> completePlace() }
			i++
			i %= texts.size
			// set second answer
			texts[i].text = place.answer2
			texts[i].setOnClickListener { v: View? -> showWrongAnswerToast() }
			i++
			i %= texts.size
			// set third answer
			texts[i].text = place.answer3
			texts[i].setOnClickListener { v: View? -> showWrongAnswerToast() }
			// Set view
			dialogBuilder.setView(dialogView)
		} else {
			dialogBuilder.setTitle(getString(R.string.place_completed)) //
					.setPositiveButton(R.string.next) { dialogInterface: DialogInterface?, i: Int -> completePlace() }
					.setOnDismissListener { dialogInterface: DialogInterface? -> requestLocationUpdates() }
		}
		questionDialog = dialogBuilder.create()
		questionDialog!!.show()
	}

	private fun showWrongAnswerToast() {
		Log.d(TAG(), "showWrongAnswerToast: clicked wrong answer")
		if (questionDialog != null && questionDialog!!.isShowing) questionDialog!!.dismiss()
		Toast.makeText(requireContext(), getString(R.string.wrong_answer), Toast.LENGTH_SHORT)
				.show()
		requestLocationUpdates()
	}

	companion object {
		const val TOUR_ID = "TOUR_ID"
		private const val PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION = 1
		private const val DISTANCE_TO_COMPLETE = 15f
		private const val LOC_TIME_REQ: Long = 200
		private const val LOC_DIST_REQ = 2f
	}
}