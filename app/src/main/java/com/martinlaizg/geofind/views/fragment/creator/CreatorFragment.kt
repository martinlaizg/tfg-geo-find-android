package com.martinlaizg.geofind.views.fragment.creator

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.views.adapter.CreatorPlacesAdapter
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel

class CreatorFragment : Fragment(), View.OnClickListener {

	private var tourName: TextView? = null
	private var tourDescription: TextView? = null
	private var addPlaceButton: MaterialButton? = null
	private var createTourButton: MaterialButton? = null
	private var editButton: MaterialButton? = null
	private var placesList: RecyclerView? = null

	private var viewModel: CreatorViewModel? = null
	private var adapter: CreatorPlacesAdapter? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_creator, container, false)
		tourName = view.findViewById(R.id.tour_name)
		tourDescription = view.findViewById(R.id.tour_description)
		addPlaceButton = view.findViewById(R.id.add_place_button)
		createTourButton = view.findViewById(R.id.create_tour_button)
		editButton = view.findViewById(R.id.edit_button)
		placesList = view.findViewById(R.id.places_list)

		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
		adapter = CreatorPlacesAdapter()
		placesList!!.layoutManager = LinearLayoutManager(requireActivity())
		placesList!!.adapter = adapter
		val helper = ItemTouchHelper(
				object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
					override fun onMove(recyclerView: RecyclerView,
					                    dragged: RecyclerView.ViewHolder,
					                    target: RecyclerView.ViewHolder): Boolean {
						adapter!!.onItemMove(dragged.adapterPosition, target.adapterPosition)
						return true
					}

					override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
						// Do nothing
					}
				})
		helper.attachToRecyclerView(placesList)

		// Back button callback
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
		viewModel!!.getTour(tourId).observe(viewLifecycleOwner, Observer { tour: Tour? -> setTour(tour) })
		addPlaceButton!!.setOnClickListener {
			val c = Bundle()
			c.putInt(CreatePlaceFragment.PLACE_POSITION, viewModel!!.places.size)
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toCreatePlace, c)
		}
		val finalTourId = tourId
		editButton!!.setOnClickListener {
			val c = Bundle()
			c.putInt(CreateTourFragment.TOUR_ID, finalTourId)
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toEditTour, c)
		}
		createTourButton!!.setOnClickListener(this)
	}

	private fun setTour(tour: Tour?) {
		if (tour == null) {
			viewModel!!.error
			return
		}
		adapter!!.places = tour.places
		adapter!!.fragmentActivity = requireActivity()
		if (tour.id != 0) {
			createTourButton!!.setText(R.string.update_tour)
		}
		if (tour.name.isEmpty()) {
			tourName!!.text = getString(R.string.click_edit)
		} else {
			tourName!!.text = tour.name
		}
		if (tour.description.isEmpty()) {
			tourDescription!!.text = resources.getString(R.string.without_description)
		} else {
			tourDescription!!.text = tour.description
		}
	}

	override fun onDestroy() {
		super.onDestroy()
		viewModel!!.reset()
	}

	override fun onClick(v: View) {
		if (viewModel!!.storedTour!!.places.size == 0) {
			Toast.makeText(requireContext(), getString(R.string.at_least_one_place),
					Toast.LENGTH_SHORT).show()
			return
		}
		createTourButton!!.isEnabled = false
		viewModel!!.createTour().observe(this, Observer { tour: Tour? ->
			createTourButton!!.isEnabled = true
			if (tour == null) {
				val err = viewModel!!.error
				Toast.makeText(requireActivity(), err.toString(), Toast.LENGTH_LONG).show()
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.popBackStack(R.id.navTourList, false)
				return@Observer
			}
			Toast.makeText(requireActivity(), R.string.tour_created, Toast.LENGTH_SHORT).show()
			val b = Bundle()
			b.putInt(TourFragment.TOUR_ID, tour.id)
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toNewTour, b)
		})
	}

	companion object {
		const val TOUR_ID = "TOUR_ID"
	}
}