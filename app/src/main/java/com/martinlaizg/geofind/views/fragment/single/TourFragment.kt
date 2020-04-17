package com.martinlaizg.geofind.views.fragment.single

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.google.android.material.chip.Chip
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.data.enums.PlayLevel
import com.martinlaizg.geofind.views.adapter.PlaceListAdapter
import com.martinlaizg.geofind.views.fragment.creator.CreatorFragment
import com.martinlaizg.geofind.views.fragment.play.PlayTourFragment
import com.martinlaizg.geofind.views.viewmodel.TourViewModel
import java.util.*

class TourFragment : Fragment() {
    @kotlin.jvm.JvmField
    @BindView(R.id.tour_name)
    var tour_name: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_description)
    var tour_description: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_creator)
    var tour_creator: Chip? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_num_places)
    var tour_num_places: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.edit_button)
    var edit_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.places_list)
    var places_list: RecyclerView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.other_places)
    var other_places: RecyclerView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.completed_text)
    var completed_text: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.completed_divider)
    var completed_divider: View? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.in_progress_text)
    var in_progress_text: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.in_progress_divider)
    var in_progress_divider: View? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.play_button)
    var play_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.empty_text)
    var empty_text: TextView? = null
    private var adapterCompleted: PlaceListAdapter? = null
    private var adapterNoCompleted: PlaceListAdapter? = null
    private var alert: AlertDialog? = null
    private var viewModel: TourViewModel? = null
    private var user: User? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_tour, container, false)
        ButterKnife.bind(this, view)
        tour_description!!.movementMethod = ScrollingMovementMethod()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sp = PreferenceManager.getDefaultSharedPreferences(requireActivity())
        user = Preferences.getLoggedUser(sp)
        adapterCompleted = PlaceListAdapter(true)
        places_list!!.layoutManager = LinearLayoutManager(requireActivity())
        places_list!!.adapter = adapterCompleted
        adapterNoCompleted = PlaceListAdapter(false)
        other_places!!.layoutManager = LinearLayoutManager(requireActivity())
        other_places!!.adapter = adapterNoCompleted
        var tourId = 0
        val b = arguments
        if (b != null) {
            tourId = b.getInt(TOUR_ID)
        }
        if (tourId == 0) {
            Toast.makeText(requireContext(),
                    requireContext().getString(R.string.tour_not_permitted),
                    Toast.LENGTH_SHORT).show()
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .popBackStack()
        }
        viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel::class.java)
        viewModel!!.getTour(tourId, user.getId()).observe(this, Observer { tour: Tour? -> setTour(tour) })
    }

    private fun setTour(tour: Tour?) {
        if (tour != null) {
            tour_name!!.text = tour.name
            tour_description!!.text = tour.description
            tour_creator.setText(tour.creator.username)
            if (tour.places.isEmpty()) {
                empty_text!!.visibility = View.VISIBLE
            }
            if (user != null && user.getId() == tour.creator_id) {
                val b = Bundle()
                b.putInt(CreatorFragment.Companion.TOUR_ID, tour.id)
                edit_button!!.setOnClickListener(
                        Navigation.createNavigateOnClickListener(R.id.toEditCreator, b))
                edit_button!!.visibility = View.VISIBLE
            }
            setPlaces(tour)
        } else {
            val error = viewModel.getError()
            if (error == null) {
                Toast.makeText(requireContext(), getString(R.string.something_went_wrong),
                        Toast.LENGTH_SHORT).show()
                Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                        .popBackStack()
            }
        }
    }

    private fun setPlaces(tour: Tour) {
        val places: List<Place?> = ArrayList(tour.places)
        val playPlaces = viewModel.getPlayPlaces()
        val numTotalPlaces = places.size
        tour_num_places!!.text = resources
                .getQuantityString(R.plurals.number_place, numTotalPlaces,
                        numTotalPlaces)
        play_button!!.setOnClickListener { v: View? -> alert!!.show() }
        setDifficultyDialog(tour.id, tour.min_level)
        if (playPlaces.size == 0) {
            // Not played yet
            in_progress_text!!.text = getString(R.string.places)
            adapterNoCompleted!!.setPlaces(places)
            completed_divider!!.visibility = View.GONE
            completed_text!!.visibility = View.GONE
            places_list!!.visibility = View.GONE
            return
        }

        // In progress
        adapterCompleted!!.setPlaces(playPlaces)
        var i = 0
        while (i < places.size) {
            for (p in playPlaces) {
                if (places[i].getId() == p.id) {
                    places.removeAt(i)
                    i--
                    break
                }
            }
            i++
        }
        adapterNoCompleted!!.setPlaces(places)
        if (places.size == 0) {
            play_button!!.text = getString(R.string.completed)
            play_button!!.isEnabled = false
            in_progress_text!!.visibility = View.GONE
            in_progress_divider!!.visibility = View.GONE
        }
    }

    private fun setDifficultyDialog(tourId: Int, minLevel: PlayLevel?) {
        val playArguments = Bundle()
        playArguments.putInt(PlayTourFragment.Companion.TOUR_ID, tourId)

        // Get all of the difficulties
        var difficulties: List<String> = ArrayList(
                Arrays.asList(*resources.getStringArray(R.array.difficulties)))
        val numDiff = difficulties.size
        // Get only the allowed difficulties
        difficulties = difficulties.subList(minLevel!!.ordinal, difficulties.size)

        // Copy difficulties
        val items = arrayOfNulls<String>(difficulties.size)
        for (i in difficulties.indices) {
            items[i] = difficulties[i]
        }
        val offset = numDiff - items.size
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(resources.getString(R.string.select_play_difficulty))
        builder.setItems(items) { dialog: DialogInterface?, item: Int ->
            val destinations = intArrayOf(R.id.toPlayTour, R.id.toPlayCompass, R.id.toPlayTherm)
            val destPos = item + offset
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .navigate(destinations[destPos], playArguments)
        }
        alert = builder.create()
    }

    companion object {
        const val TOUR_ID = "TOUR_ID"
    }
}