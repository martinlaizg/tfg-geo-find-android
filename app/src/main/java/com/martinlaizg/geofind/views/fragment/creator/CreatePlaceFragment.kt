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
import butterknife.BindView
import butterknife.ButterKnife
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
import com.squareup.picasso.Picasso
import java.io.IOException
import java.util.*

class CreatePlaceFragment : Fragment(), View.OnClickListener, OnMapReadyCallback {
    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_name_layout)
    var new_place_name: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_description_layout)
    var new_place_description: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_create_button)
    var create_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_map_view)
    var new_place_map_view: MapView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_address)
    var new_place_address: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_location_button)
    var new_place_location_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_image_button)
    var new_place_image_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.place_image_view)
    var place_image_view: ImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.question_switch)
    var question_switch: Switch? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.question_layout)
    var question_layout: ConstraintLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_question)
    var new_place_question: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_correct_answer)
    var new_place_correct_answer: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_answer_2)
    var new_place_answer_2: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.new_place_answer_3)
    var new_place_answer_3: TextInputLayout? = null
    private var viewModel: CreatorViewModel? = null
    private var googleMap: GoogleMap? = null
    private var image_url: String? = null
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
        val placeName = Objects.requireNonNull(new_place_name!!.editText).text.toString()
                .trim { it <= ' ' }
        if (placeName.isEmpty()) {
            new_place_name!!.error = getString(R.string.required_name)
            return false
        }
        if (placeName.length > resources.getInteger(R.integer.max_name_length)) {
            new_place_name!!.error = getString(R.string.text_too_long)
            return false
        }
        new_place_name!!.error = ""

        // Get the description
        val placeDescription = Objects.requireNonNull(new_place_description!!.editText)
                .text.toString().trim { it <= ' ' }
        if (placeDescription.isEmpty()) {
            new_place_description!!.error = getString(R.string.required_description)
            return false
        }
        if (placeDescription.length >
                resources.getInteger(R.integer.max_description_length)) {
            new_place_description!!.error = getString(R.string.text_too_long)
            return false
        }
        new_place_description!!.error = ""

        // Get the question
        val question = storeQuestion()
        if (!question) return false
        viewModel.getPlace().name = placeName
        viewModel.getPlace().description = placeDescription
        return true
    }

    /**
     * Store the question data into the viewModel [Place]
     *
     * @return true if no has errors else false
     */
    private fun storeQuestion(): Boolean {
        if (!question_switch!!.isChecked) {
            viewModel.getPlace().question = null
            viewModel.getPlace().answer = null
            viewModel.getPlace().answer2 = null
            viewModel.getPlace().answer3 = null
            return true
        }

        // Check the question
        val question = Objects.requireNonNull(new_place_question!!.editText).text
                .toString().trim { it <= ' ' }
        new_place_question!!.error = ""
        if (question.isEmpty()) {
            new_place_question!!.error = getString(R.string.required_question)
            return false
        }

        // Check the correct answer
        val correctAnswer = Objects.requireNonNull(new_place_correct_answer!!.editText)
                .text.toString().trim { it <= ' ' }
        new_place_correct_answer!!.error = ""
        if (correctAnswer.isEmpty()) {
            new_place_correct_answer!!.error = getString(R.string.required_answer)
            return false
        }

        // Check the second answer
        val secondAnswer = Objects.requireNonNull(new_place_answer_2!!.editText).text
                .toString().trim { it <= ' ' }
        new_place_answer_2!!.error = ""
        if (secondAnswer.isEmpty()) {
            new_place_answer_2!!.error = getString(R.string.required_answer)
            return false
        }

        // Check the third answer
        val thirdAnswer = Objects.requireNonNull(new_place_answer_3!!.editText).text
                .toString().trim { it <= ' ' }
        new_place_answer_3!!.error = ""
        if (thirdAnswer.isEmpty()) {
            new_place_answer_3!!.error = getString(R.string.required_answer)
            return false
        }
        if (secondAnswer == thirdAnswer) {
            new_place_answer_3!!.error = getString(R.string.answer_repeated)
            return false
        }

        // Set the values
        viewModel.getPlace()
                .setCompleteQuestion(question, correctAnswer, secondAnswer, thirdAnswer)
        return true
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        this.googleMap!!.uiSettings.setAllGesturesEnabled(false)
        this.googleMap!!.clear()
        setPosition(viewModel.getPlace())
    }

    private fun setPosition(place: Place?) {
        if (place == null) return
        val position = place.position ?: return

        // Change button text
        new_place_location_button!!.text = getString(R.string.update)
        new_place_address!!.visibility = View.VISIBLE

        // Get address
        val gc = Geocoder(requireContext())
        val locations: List<Address>?
        new_place_address
                .setText(getString(R.string.two_csv, position.latitude, position.longitude))
        try {
            locations = gc.getFromLocation(position.latitude, position.longitude, 1)
            if (locations != null && locations.size >= 1) {
                new_place_address!!.text = locations[0].getAddressLine(0)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_create_place, container, false)
        ButterKnife.bind(this, view)
        // Back button listener
        requireActivity().onBackPressedDispatcher
                .addCallback(this, object : OnBackPressedCallback(true) {
                    override fun handleOnBackPressed() {
                        showExitDialog()
                    }
                })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // Show/hide question layout
        question_switch!!.setOnCheckedChangeListener { buttonView: CompoundButton?, isChecked: Boolean ->
            question_layout
                    .setVisibility(if (isChecked) View.VISIBLE else View.GONE)
        }
        viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
        val b = arguments
        if (b != null) {
            val position = b.getInt(PLACE_POSITION, viewModel.getPlaces().size)
            viewModel!!.retrievePlace(position)
            setPlace()
        }
        create_button!!.setOnClickListener(this)
        new_place_image_button!!.setOnClickListener { v: View? ->
            val alertDialog = buildDialog()
            alertDialog.show()
        }
        new_place_location_button!!.setOnClickListener { v: View? ->
            if (!storePlace()) return@setOnClickListener
            KeyboardUtils.hideKeyboard(requireActivity())
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .navigate(R.id.toCreatePlaceLocation)
        }
    }

    private fun setPlace() {
        val place = viewModel.getPlace()
        if (place == null) {
            Toast.makeText(requireContext(), getString(R.string.something_went_wrong),
                    Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .popBackStack()
            return
        }

        // Set name
        if (place.name != null) {
            Objects.requireNonNull(new_place_name!!.editText).setText(place.name)
        }

        // Set description
        if (place.description != null) {
            Objects.requireNonNull(new_place_description!!.editText)
                    .setText(place.description)
        }
        // Set location marker
        setPosition(place)

        // Set question
        question_switch!!.isChecked = false
        if (place.question != null) {
            question_switch!!.isChecked = true
            Objects.requireNonNull(new_place_question!!.editText).setText(place.question)
            Objects.requireNonNull(new_place_correct_answer!!.editText)
                    .setText(place.answer)
            Objects.requireNonNull(new_place_answer_2!!.editText).setText(place.answer2)
            Objects.requireNonNull(new_place_answer_3!!.editText).setText(place.answer3)
        }

        // Set image
        place_image_view!!.visibility = View.GONE
        if (place.image != null && !place.image.isEmpty()) {
            image_url = place.image
            Picasso.with(requireContext()).load(image_url).into(place_image_view)
            place_image_view!!.visibility = View.VISIBLE
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
            place_image_view!!.visibility = View.GONE
            if (!image_url!!.isEmpty()) {
                Picasso.with(requireContext()).load(image_url).into(place_image_view)
                place_image_view!!.visibility = View.VISIBLE
            }
            KeyboardUtils.hideKeyboard(requireActivity())
        }.create()
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

    companion object {
        const val PLACE_POSITION = "PLACE_POSITION"
        private val TAG = CreatePlaceFragment::class.java.simpleName
        private const val CAMERA_UPDATE_ZOOM = 15
    }
}