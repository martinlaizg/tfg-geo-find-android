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
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.viewmodel.PlayTourViewModel
import java.util.*
import kotlin.math.roundToInt

abstract class PlayTourFragment : Fragment(), LocationListener {

	var place: Place? = null
		set(value) {
			distance = Float.MAX_VALUE
			requestLocationUpdates()
			field = value
			placeLocation = place!!.location
			placeName!!.text = place!!.name
			placeDescription!!.text = place!!.description
			val numCompletedPlaces = viewModel!!.play!!.places.size + 1
			val numPlaces = viewModel!!.play!!.tour!!.places.size
			placeComplete!!.text = resources.getQuantityString(R.plurals.place_number_number,
					numCompletedPlaces,
					numCompletedPlaces, numPlaces)
			updateView()
		}
	var usrLocation: Location? = null
	var placeLocation: Location? = null
	var distance = Float.MAX_VALUE

	private var viewModel: PlayTourViewModel? = null
	private var locationManager: LocationManager? = null
	private var questionDialog: AlertDialog? = null

	private var placeName: TextView? = null
	private var placeDescription: TextView? = null
	private var placeComplete: TextView? = null
	private var placeDistance: TextView? = null

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		placeName = view.findViewById(R.id.place_name)
		placeDescription = view.findViewById(R.id.place_description)
		placeComplete = view.findViewById(R.id.place_complete)
		placeDistance = view.findViewById(R.id.place_distance)
		viewModel = ViewModelProviders.of(requireActivity()).get(PlayTourViewModel::class.java)
		val b = arguments
		var tourId = 0
		if (b != null) {
			tourId = b.getInt(TOUR_ID)
		}
		val u = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()))
		viewModel!!.loadPlay(u.id, tourId).observe(viewLifecycleOwner, Observer { newPlace: Place? ->
			if (newPlace == null) {
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack(R.id.navTour, false)
				return@Observer
			}
			this.place = newPlace
		})
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
	                                        grantResults: IntArray) {
		if (requestCode == PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION && permissions.size >= 2) {
			if (permissions[0] == permission.ACCESS_COARSE_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED && permissions[1] == permission.ACCESS_FINE_LOCATION && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
				this.place = place
				return
			}
			requestPermissions(arrayOf(permission.ACCESS_COARSE_LOCATION,
					permission.ACCESS_FINE_LOCATION),
					PERMISSION_ACCESS_COARSE_AND_FINE_LOCATION)
		}
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
		locationManager!!.removeUpdates(this)
	}

	private fun requestLocationUpdates() {
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
		locationManager!!.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOC_TIME_REQ, LOC_DIST_REQ, this)
	}

	abstract fun updateView()

	@SuppressLint("MissingPermission")
	override fun onLocationChanged(location: Location) {
		Log.d(tag, "onLocationChanged: ")
		usrLocation = location

		// Set distance
		if (placeLocation != null) {
			distance = usrLocation!!.distanceTo(placeLocation)
			if (distance > 1000f) {
				var newDistance = distance / 1000
				newDistance = (newDistance * 100f).roundToInt() / 100f
				placeDistance!!.text = resources.getString(R.string.place_distance_km, newDistance)
			} else {
				placeDistance!!.text = resources.getString(R.string.place_distance_m, distance.toInt())
			}
			if (distance < DISTANCE_TO_COMPLETE) {
				removeLocationUpdates()
				showCompleteDialog(place)
				return
			}
		}
		updateView()
	}

	override fun onStatusChanged(provider: String, status: Int, extras: Bundle) = Unit

	override fun onProviderEnabled(provider: String) = Unit

	override fun onProviderDisabled(provider: String) = Unit

	private fun completePlace() {
		if (questionDialog != null && questionDialog!!.isShowing) questionDialog!!.dismiss()
		viewModel!!.completePlace(place!!.id).observe(this, Observer { nextPlace: Place? ->
			if (nextPlace == null) {
				if (viewModel!!.tourIsCompleted()) {
					completeTour()
					return@Observer
				}
			} else {
				this.place = nextPlace
			}
		})
	}

	private fun completeTour() {
		MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.tour_completed) //
				.setPositiveButton(R.string.ok) { dialog: DialogInterface, _: Int -> dialog.dismiss() }
				.setOnDismissListener {
					Navigation
							.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack(R.id.navTour, false)
				}.show()
	}

	private fun showCompleteDialog(place: Place?) {
		val dialogBuilder = MaterialAlertDialogBuilder(requireContext())
		if (place?.question != null && place.question!!.isNotEmpty()) {
			// Inflate view
			val dialogView = layoutInflater
					.inflate(R.layout.question_layout, ConstraintLayout(requireContext()),
							false)
			// Set question
			val question = dialogView.findViewById<TextView>(R.id.question)
			question.text = place.question
			val texts: List<MaterialButton> = listOf(dialogView.findViewById(R.id.answer1),
					dialogView.findViewById(R.id.answer2),
					dialogView.findViewById(R.id.answer3))
			// Set answers
			// Get random position to start
			var i = Random().nextInt(texts.size)

			// Set correct answer
			texts[i].text = place.answer
			texts[i].setOnClickListener { completePlace() }
			i++
			i %= texts.size
			// set second answer
			texts[i].text = place.answer2
			texts[i].setOnClickListener { showWrongAnswerToast() }
			i++
			i %= texts.size
			// set third answer
			texts[i].text = place.answer3
			texts[i].setOnClickListener { showWrongAnswerToast() }
			// Set view
			dialogBuilder.setView(dialogView)
		} else {
			dialogBuilder.setTitle(getString(R.string.place_completed)) //
					.setPositiveButton(R.string.next) { _: DialogInterface?, _: Int -> completePlace() }
					.setOnDismissListener { requestLocationUpdates() }
		}
		questionDialog = dialogBuilder.create()
		questionDialog!!.show()
	}

	private fun showWrongAnswerToast() {
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