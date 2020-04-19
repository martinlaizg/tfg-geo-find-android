package com.martinlaizg.geofind.views.fragment.creator

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.enums.PlayLevel
import com.martinlaizg.geofind.utils.KeyboardUtils
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel

class CreateTourFragment : Fragment(), View.OnClickListener {

	private var tourNameLayout: TextInputLayout? = null
	private var tourDescriptionLayout: TextInputLayout? = null
	private var tourImageView: ImageView? = null
	private var doneButton: MaterialButton? = null
	private var addImageButton: MaterialButton? = null
	private var difficultySpinner: Spinner? = null

	private var viewModel: CreatorViewModel? = null
	private var imageUrl: String? = ""

	companion object {
		const val TOUR_ID = "TOUR_ID"
		private val TAG = CreateTourFragment::class.java.simpleName
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_create_tour, container, false)
		tourNameLayout = view.findViewById(R.id.tour_name_layout)
		tourDescriptionLayout = view.findViewById(R.id.tour_description_layout)
		tourImageView = view.findViewById(R.id.tour_image_view)
		doneButton = view.findViewById(R.id.done_button)
		addImageButton = view.findViewById(R.id.add_image_button)
		difficultySpinner = view.findViewById(R.id.difficulty_spinner)

		val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
			override fun handleOnBackPressed() {
				showExitDialog()
			}
		}
		requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
		return view
	}

	private fun showExitDialog() {
		MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
				.setMessage(getString(R.string.exit_lose_data_alert))
				.setPositiveButton(getString(R.string.ok)) { _: DialogInterface?, _: Int ->
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack()
				}.show()
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val b = arguments
		var tourId = 0
		if (b != null) {
			tourId = b.getInt(TOUR_ID)
		}
		Log.i(TAG, "onViewCreated: Get tour with id = $tourId")
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
		viewModel!!.getTour(tourId).observe(viewLifecycleOwner, Observer { tour: Tour? -> setTour(tour) })
		doneButton!!.setOnClickListener(this)
		addImageButton!!.setOnClickListener {
			val alertDialog = buildDialog()
			alertDialog.show()
		}
	}

	private fun setTour(tour: Tour?) {
		if (tour == null) {
			val error = viewModel!!.error
			Log.e(TAG, "setTour: Error getting the tour$error")
			if (error == ErrorType.EXIST) {
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack()
			}
			return
		}
		tourNameLayout!!.editText!!.setText(tour.name)
		tourDescriptionLayout!!.editText!!.setText(tour.description)
		difficultySpinner!!.setSelection(tour.minLevel.ordinal)
		tourImageView!!.visibility = View.GONE
		if (tour.image != null) imageUrl = tour.image
		if (imageUrl!!.isNotEmpty()) {
			tourImageView!!.visibility = View.VISIBLE
			TODO("load image")
		}
		if (tour.id > 0) {
			doneButton!!.text = getString(R.string.update)
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
			tourImageView!!.visibility = View.GONE
			if (imageUrl!!.isNotEmpty()) {
				tourImageView!!.visibility = View.VISIBLE
				TODO("load image")
			}
			KeyboardUtils.hideKeyboard(requireActivity())
		}.create()
	}

	override fun onClick(v: View) {
		KeyboardUtils.hideKeyboard(requireActivity())
		tourNameLayout!!.error = ""
		val name = tourNameLayout!!.editText!!.text.toString().trim()
		if (name.isEmpty()) {
			tourNameLayout!!.error = getString(R.string.required_name)
			return
		}
		tourNameLayout!!.error = ""
		if (name.length > resources.getInteger(R.integer.max_name_length)) {
			tourNameLayout!!.error = getString(R.string.text_too_long)
			return
		}
		tourDescriptionLayout!!.error = ""
		val description = tourDescriptionLayout!!.editText!!.text.toString().trim()
		if (description.isEmpty()) {
			tourDescriptionLayout!!.error = getString(R.string.required_description)
			return
		}
		if (description.length > resources.getInteger(R.integer.max_description_length)) {
			tourDescriptionLayout!!.error = getString(R.string.text_too_long)
			return
		}
		val pl: PlayLevel = PlayLevel.getPlayLevel(difficultySpinner!!.selectedItemPosition)
		val user = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()))
		viewModel!!.updateTour(name, description, user.id, pl, imageUrl)
		val id = viewModel!!.storedTour!!.id
		val args = Bundle()
		args.putInt(CreatorFragment.TOUR_ID, id)
		Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).navigate(R.id.toCreator, args)
	}
}