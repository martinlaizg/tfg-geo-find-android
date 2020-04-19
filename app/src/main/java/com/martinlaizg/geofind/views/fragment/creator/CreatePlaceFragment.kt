package com.martinlaizg.geofind.views.fragment.creator

import android.app.AlertDialog
import android.content.DialogInterface
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.utils.KeyboardUtils
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel
import java.io.IOException

class CreatePlaceFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {

	private var newPlaceMapView: MapView? = null
	private var newPlaceAddress: TextView? = null
	private var placeImageView: ImageView? = null

	private var newPlaceName: TextInputLayout? = null
	private var newPlaceDescription: TextInputLayout? = null

	private var createButton: MaterialButton? = null
	private var newPlaceLocationButton: MaterialButton? = null
	private var newPlaceImageButton: MaterialButton? = null
	private var questionSwitch: Switch? = null

	private var questionLayout: ConstraintLayout? = null
	private var newPlaceQuestion: TextInputLayout? = null
	private var newPlaceCorrectAnswer: TextInputLayout? = null
	private var newPlaceAnswer2: TextInputLayout? = null
	private var newPlaceAnswer3: TextInputLayout? = null

	private var viewModel: CreatorViewModel? = null
	private var googleMap: GoogleMap? = null
	private var imageUrl: String? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_create_place, container, false)

		newPlaceMapView = view.findViewById(R.id.new_place_map_view)
		newPlaceAddress = view.findViewById(R.id.new_place_address)
		placeImageView = view.findViewById(R.id.place_image_view)

		newPlaceName = view.findViewById(R.id.new_place_name_layout)
		newPlaceDescription = view.findViewById(R.id.new_place_description_layout)

		createButton = view.findViewById(R.id.new_place_create_button)
		newPlaceLocationButton = view.findViewById(R.id.new_place_location_button)
		newPlaceImageButton = view.findViewById(R.id.new_place_image_button)
		questionSwitch = view.findViewById(R.id.question_switch)

		questionLayout = view.findViewById(R.id.question_layout)
		newPlaceQuestion = view.findViewById(R.id.new_place_question)
		newPlaceCorrectAnswer = view.findViewById(R.id.new_place_correct_answer)
		newPlaceAnswer2 = view.findViewById(R.id.new_place_answer_2)
		newPlaceAnswer3 = view.findViewById(R.id.new_place_answer_3)

		// Back button listener
		requireActivity().onBackPressedDispatcher
				.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
					override fun handleOnBackPressed() {
						showExitDialog()
					}
				})
		return view
	}

	override fun onClick(v: View) {
		KeyboardUtils.hideKeyboard(requireActivity())
		if (!storePlace()) return
		viewModel!!.savePlace()
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack()
	}

	/**
	 * Store the place data (name, description, image, question) into the viewModel [Place]
	 *
	 * @return true if no has errors else false
	 */
	private fun storePlace(): Boolean {
		// Get the name
		val placeName = newPlaceName!!.editText!!.text.toString().trim()
		if (placeName.isEmpty()) {
			newPlaceName!!.error = getString(R.string.required_name)
			return false
		}
		if (placeName.length > resources.getInteger(R.integer.max_name_length)) {
			newPlaceName!!.error = getString(R.string.text_too_long)
			return false
		}
		newPlaceName!!.error = ""

		// Get the description
		val placeDescription = newPlaceDescription!!.editText!!.text.toString().trim()
		if (placeDescription.isEmpty()) {
			newPlaceDescription!!.error = getString(R.string.required_description)
			return false
		}
		if (placeDescription.length > resources.getInteger(R.integer.max_description_length)) {
			newPlaceDescription!!.error = getString(R.string.text_too_long)
			return false
		}
		newPlaceDescription!!.error = ""

		// Get the question
		val question = storeQuestion()
		if (!question) return false
		viewModel!!.place!!.name = placeName
		viewModel!!.place!!.description = placeDescription
		return true
	}

	/**
	 * Store the question data into the viewModel [Place]
	 *
	 * @return true if no has errors else false
	 */
	private fun storeQuestion(): Boolean {
		if (!questionSwitch!!.isChecked) {
			viewModel!!.place!!.question = null
			viewModel!!.place!!.answer = null
			viewModel!!.place!!.answer2 = null
			viewModel!!.place!!.answer3 = null
			return true
		}

		// Check the question
		val question = newPlaceQuestion!!.editText!!.text.toString().trim()
		newPlaceQuestion!!.error = ""
		if (question.isEmpty()) {
			newPlaceQuestion!!.error = getString(R.string.required_question)
			return false
		}

		// Check the correct answer
		val correctAnswer = newPlaceCorrectAnswer!!.editText!!.text.toString().trim()
		newPlaceCorrectAnswer!!.error = ""
		if (correctAnswer.isEmpty()) {
			newPlaceCorrectAnswer!!.error = getString(R.string.required_answer)
			return false
		}

		// Check the second answer
		val secondAnswer = newPlaceAnswer2!!.editText!!.text.toString().trim()
		newPlaceAnswer2!!.error = ""
		if (secondAnswer.isEmpty()) {
			newPlaceAnswer2!!.error = getString(R.string.required_answer)
			return false
		}

		// Check the third answer
		val thirdAnswer = newPlaceAnswer3!!.editText!!.text.toString().trim()
		newPlaceAnswer3!!.error = ""
		if (thirdAnswer.isEmpty()) {
			newPlaceAnswer3!!.error = getString(R.string.required_answer)
			return false
		}
		if (secondAnswer == thirdAnswer) {
			newPlaceAnswer3!!.error = getString(R.string.answer_repeated)
			return false
		}

		// Set the values
		viewModel!!.place!!.fillQuestion(question, correctAnswer, secondAnswer, thirdAnswer)
		return true
	}

	override fun onMapReady(googleMap: GoogleMap) {
		this.googleMap = googleMap
		this.googleMap!!.uiSettings.setAllGesturesEnabled(false)
		this.googleMap!!.clear()
		setPosition(viewModel!!.place)
	}

	private fun setPosition(place: Place?) {
		if (place == null) return
		val position = place.position ?: return

		// Change button text
		newPlaceLocationButton!!.text = getString(R.string.update)
		newPlaceAddress!!.visibility = View.VISIBLE

		// Get address
		val gc = Geocoder(requireContext())
		val locations: List<Address>?
		newPlaceAddress!!.text = getString(R.string.two_csv, position.latitude, position.longitude)
		try {
			locations = gc.getFromLocation(position.latitude, position.longitude, 1)
			if (locations != null && locations.isNotEmpty()) {
				newPlaceAddress!!.text = locations[0].getAddressLine(0)
			}
		} catch (e: IOException) {
			e.printStackTrace()
		}
		if (googleMap != null) {
			// Set static map
			val marker = MarkerOptions().position(position)
			googleMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(position, CAMERA_UPDATE_ZOOM.toFloat()))
			googleMap!!.addMarker(marker)
		}
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		// Show/hide question layout
		questionSwitch!!.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
			questionLayout!!.visibility = if (isChecked) View.VISIBLE else View.GONE
		}
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
		val b = arguments
		if (b != null) {
			val position = b.getInt(PLACE_POSITION, viewModel!!.places.size)
			viewModel!!.retrievePlace(position)
			setPlace()
		}
		createButton!!.setOnClickListener(this)
		newPlaceImageButton!!.setOnClickListener {
			val alertDialog = buildDialog()
			alertDialog.show()
		}
		newPlaceLocationButton!!.setOnClickListener {
			if (!storePlace()) return@setOnClickListener
			KeyboardUtils.hideKeyboard(requireActivity())
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toCreatePlaceLocation)
		}
	}

	private fun setPlace() {
		val place = viewModel!!.place
		if (place == null) {
			Toast.makeText(requireContext(), getString(R.string.something_went_wrong),
					Toast.LENGTH_SHORT).show()
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack()
			return
		}

		// Set name
		newPlaceName!!.editText!!.setText(place.name)

		// Set description
		newPlaceDescription!!.editText!!.setText(place.description)
		// Set location marker
		setPosition(place)

		// Set question
		questionSwitch!!.isChecked = false
		if (place.question != null) {
			questionSwitch!!.isChecked = true
			newPlaceQuestion!!.editText!!.setText(place.question)
			newPlaceCorrectAnswer!!.editText!!.setText(place.answer)
			newPlaceAnswer2!!.editText!!.setText(place.answer2)
			newPlaceAnswer3!!.editText!!.setText(place.answer3)
		}

		// Set image
		placeImageView!!.visibility = View.GONE
		if (place.image != null && place.image!!.isNotEmpty()) {
			imageUrl = place.image
			placeImageView!!.visibility = View.VISIBLE
			TODO("load image")
		}
	}

	private fun buildDialog(): AlertDialog {
		val builder = AlertDialog.Builder(activity)
		// Get the layout inflater
		val inflater = requireActivity().layoutInflater
		val view = inflater.inflate(R.layout.add_image_layout, LinearLayout(requireContext()))
		val imageUrlLayout: TextInputLayout = view.findViewById(R.id.image_url_layout)
		imageUrlLayout.editText!!.setText(imageUrl)
		return builder.setView(view).setPositiveButton(R.string.ok) { _: DialogInterface?, _: Int ->
			imageUrl = imageUrlLayout.editText!!.text.toString()
			placeImageView!!.visibility = View.GONE
			if (imageUrl!!.isNotEmpty()) {
				TODO("load image")
			}
			KeyboardUtils.hideKeyboard(requireActivity())
		}.create()
	}

	private fun showExitDialog() {
		MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
				.setMessage(getString(R.string.exit_lose_data_alert))
				.setPositiveButton(getString(R.string.ok)) { _: DialogInterface?, _: Int ->
					Navigation
							.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack()
				}.show()
	}

	companion object {
		val PLACE_POSITION = "PLACE_POSITION"
		val CAMERA_UPDATE_ZOOM = 15
	}
}