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
import butterknife.BindView
import butterknife.ButterKnife
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
import com.squareup.picasso.Picasso
import java.util.*

class CreateTourFragment : Fragment(), View.OnClickListener {
    @kotlin.jvm.JvmField
    @BindView(R.id.tour_name_layout)
    var tour_name_layout: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_description_layout)
    var tour_description_layout: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_image_view)
    var tour_image_view: ImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.done_button)
    var done_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.add_image_button)
    var add_image_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.difficulty_spinner)
    var difficulty_spinner: Spinner? = null
    private var viewModel: CreatorViewModel? = null
    private var image_url: String? = ""
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_tour, container, false)
        ButterKnife.bind(this, view)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                showExitDialog()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
        return view
    }

    private fun showExitDialog() {
        MaterialAlertDialogBuilder(requireContext()).setTitle(R.string.are_you_sure)
                .setMessage(getString(R.string.exit_lose_data_alert))
                .setPositiveButton(getString(R.string.ok)) { dialog: DialogInterface?, which: Int ->
                    Navigation
                            .findNavController(requireActivity(), R.id.main_fragment_holder)
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
        viewModel!!.getTour(tourId).observe(this, Observer { tour: Tour? -> setTour(tour) })
        done_button!!.setOnClickListener(this)
        add_image_button!!.setOnClickListener { v: View? ->
            val alertDialog = buildDialog()
            alertDialog.show()
        }
    }

    private fun setTour(tour: Tour?) {
        if (tour == null) {
            val error = viewModel.getError()
            Log.e(TAG, "setTour: Error getting the tour$error")
            if (error == ErrorType.EXIST) {
                Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                        .popBackStack()
            }
            return
        }
        Objects.requireNonNull(tour_name_layout!!.editText).setText(tour.name)
        Objects.requireNonNull(tour_description_layout!!.editText)
                .setText(tour.description)
        difficulty_spinner!!.setSelection(tour.min_level!!.ordinal)
        tour_image_view!!.visibility = View.GONE
        if (tour.image != null) image_url = tour.image
        if (!image_url!!.isEmpty()) {
            Picasso.with(requireContext()).load(image_url).into(tour_image_view)
            tour_image_view!!.visibility = View.VISIBLE
        }
        if (tour.id > 0) {
            done_button!!.text = getString(R.string.update)
        }
    }

    private fun buildDialog(): AlertDialog {
        val builder = AlertDialog.Builder(activity)
        // Get the layout inflater
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.add_image_layout, LinearLayout(requireContext()))
        val imageUrlLayout: TextInputLayout = view.findViewById(R.id.image_url_layout)
        Objects.requireNonNull(imageUrlLayout.editText).setText(image_url)
        return builder.setView(view).setPositiveButton(R.string.ok) { dialog: DialogInterface?, id: Int ->
            image_url = Objects.requireNonNull(imageUrlLayout.editText).text.toString()
            tour_image_view!!.visibility = View.GONE
            if (!image_url!!.isEmpty()) {
                Picasso.with(requireContext()).load(image_url).into(tour_image_view)
                tour_image_view!!.visibility = View.VISIBLE
            }
            KeyboardUtils.hideKeyboard(requireActivity())
        }.create()
    }

    override fun onClick(v: View) {
        KeyboardUtils.hideKeyboard(requireActivity())
        tour_name_layout!!.error = ""
        val name = Objects.requireNonNull(tour_name_layout!!.editText).text.toString()
                .trim { it <= ' ' }
        if (name.isEmpty()) {
            tour_name_layout!!.error = getString(R.string.required_name)
            return
        }
        tour_name_layout!!.error = ""
        if (name.length > resources.getInteger(R.integer.max_name_length)) {
            tour_name_layout!!.error = getString(R.string.text_too_long)
            return
        }
        tour_description_layout!!.error = ""
        val description = Objects.requireNonNull(tour_description_layout!!.editText).text
                .toString().trim { it <= ' ' }
        if (description.isEmpty()) {
            tour_description_layout!!.error = getString(R.string.required_description)
            return
        }
        if (description.length > resources.getInteger(R.integer.max_description_length)) {
            tour_description_layout!!.error = getString(R.string.text_too_long)
            return
        }
        val pl: PlayLevel = PlayLevel.Companion.getPlayLevel(difficulty_spinner!!.selectedItemPosition)
        val user = Preferences.getLoggedUser(PreferenceManager.getDefaultSharedPreferences(requireContext()))
        viewModel!!.updateTour(name, description, user.id, pl, image_url)
        val id = viewModel.getStoredTour().id
        val args = Bundle()
        args.putInt(CreatorFragment.Companion.TOUR_ID, id)
        Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                .navigate(R.id.toCreator, args)
    }

    companion object {
        const val TOUR_ID = "TOUR_ID"
        private val TAG = CreateTourFragment::class.java.simpleName
    }
}