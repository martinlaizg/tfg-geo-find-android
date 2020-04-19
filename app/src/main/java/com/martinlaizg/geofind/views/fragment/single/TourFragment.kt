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

	private var tourName: TextView? = null
	private var tourDescription: TextView? = null
	private var tourCreator: Chip? = null
	private var tourNumPlaces: TextView? = null
	private var editButton: MaterialButton? = null
	private var placesList: RecyclerView? = null
	private var otherPlaces: RecyclerView? = null
	private var completedText: TextView? = null
	private var completedDivider: View? = null
	private var inProgressText: TextView? = null
	private var inProgressDivider: View? = null
	private var playButton: MaterialButton? = null
	private var emptyText: TextView? = null

	private var adapterCompleted: PlaceListAdapter? = null
	private var adapterNoCompleted: PlaceListAdapter? = null
	private var alert: AlertDialog? = null
	private var viewModel: TourViewModel? = null
	private var user: User? = null
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_tour, container, false)
		tourName = view.findViewById(R.id.tour_name)
		tourDescription = view.findViewById(R.id.tour_description)
		tourCreator = view.findViewById(R.id.tour_creator)
		tourNumPlaces = view.findViewById(R.id.tour_num_places)
		editButton = view.findViewById(R.id.edit_button)
		placesList = view.findViewById(R.id.places_list)
		otherPlaces = view.findViewById(R.id.other_places)
		completedText = view.findViewById(R.id.completed_text)
		completedDivider = view.findViewById(R.id.completed_divider)
		inProgressText = view.findViewById(R.id.in_progress_text)
		inProgressDivider = view.findViewById(R.id.in_progress_divider)
		playButton = view.findViewById(R.id.play_button)
		emptyText = view.findViewById(R.id.empty_text)
		tourDescription!!.movementMethod = ScrollingMovementMethod()
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		val sp = PreferenceManager.getDefaultSharedPreferences(requireActivity())
		user = Preferences.getLoggedUser(sp)
		adapterCompleted = PlaceListAdapter(true)
		placesList!!.layoutManager = LinearLayoutManager(requireActivity())
		placesList!!.adapter = adapterCompleted
		adapterNoCompleted = PlaceListAdapter(false)
		otherPlaces!!.layoutManager = LinearLayoutManager(requireActivity())
		otherPlaces!!.adapter = adapterNoCompleted
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
		viewModel!!.getTour(tourId, user!!.id).observe(viewLifecycleOwner, Observer { tour: Tour? -> setTour(tour) })
	}

	private fun setTour(tour: Tour?) {
		if (tour != null) {
			tourName!!.text = tour.name
			tourDescription!!.text = tour.description
			tourCreator!!.text = tour.creator!!.username
			if (tour.places.isEmpty()) {
				emptyText!!.visibility = View.VISIBLE
			}
			if (user != null && user!!.id == tour.creatorId) {
				val b = Bundle()
				b.putInt(CreatorFragment.TOUR_ID, tour.id)
				editButton!!.setOnClickListener(
						Navigation.createNavigateOnClickListener(R.id.toEditCreator, b))
				editButton!!.visibility = View.VISIBLE
			}
			setPlaces(tour)
		} else {
			val error = viewModel!!.error
			if (error == null) {
				Toast.makeText(requireContext(), getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show()
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack()
			}
		}
	}

	private fun setPlaces(tour: Tour) {
		val totalPlaces: List<Place> = ArrayList(tour.places)
		val playedPlaces = viewModel!!.playPlaces
		val numTotalPlaces = totalPlaces.size
		tourNumPlaces!!.text = resources
				.getQuantityString(R.plurals.number_place, numTotalPlaces,
						numTotalPlaces)
		playButton!!.setOnClickListener { alert!!.show() }
		setDifficultyDialog(tour.id, tour.minLevel)
		if (playedPlaces.isEmpty()) {
			// Not played yet
			inProgressText!!.text = getString(R.string.places)
			adapterNoCompleted!!.places = totalPlaces
			completedDivider!!.visibility = View.GONE
			completedText!!.visibility = View.GONE
			placesList!!.visibility = View.GONE
			return
		}

		// In progress
		adapterCompleted!!.places = playedPlaces
		val playedPlacesIds = playedPlaces.map(Place::id)
		val noCompletedPlaces = totalPlaces.filter { p -> playedPlacesIds.contains(p.id) }
		adapterNoCompleted!!.places = noCompletedPlaces
		if (totalPlaces.isEmpty()) {
			playButton!!.text = getString(R.string.completed)
			playButton!!.isEnabled = false
			inProgressText!!.visibility = View.GONE
			inProgressDivider!!.visibility = View.GONE
		}
	}

	private fun setDifficultyDialog(tourId: Int, minLevel: PlayLevel?) {
		val playArguments = Bundle()
		playArguments.putInt(PlayTourFragment.TOUR_ID, tourId)

		// Get all of the difficulties
		var difficulties: List<String> = ArrayList(
				listOf(*resources.getStringArray(R.array.difficulties)))
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
		builder.setItems(items) { _: DialogInterface?, item: Int ->
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