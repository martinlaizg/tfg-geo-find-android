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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.views.adapter.CreatorPlacesAdapter
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel

class CreatorFragment : Fragment(), View.OnClickListener {
    @kotlin.jvm.JvmField
    @BindView(R.id.tour_name)
    var tour_name: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.tour_description)
    var tour_description: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.add_place_button)
    var add_place_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.create_tour_button)
    var create_tour_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.edit_button)
    var edit_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.places_list)
    var places_list: RecyclerView? = null
    private var viewModel: CreatorViewModel? = null
    private var adapter: CreatorPlacesAdapter? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_creator, container, false)
        ButterKnife.bind(this, view)
        viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
        adapter = CreatorPlacesAdapter()
        places_list!!.layoutManager = LinearLayoutManager(requireActivity())
        places_list!!.adapter = adapter
        val helper = ItemTouchHelper(
                object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0) {
                    override fun onMove(recyclerView: RecyclerView,
                                        dragged: RecyclerView.ViewHolder,
                                        target: RecyclerView.ViewHolder): Boolean {
                        adapter!!.onItemMove(dragged.adapterPosition,
                                target.adapterPosition)
                        return true
                    }

                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder,
                                          direction: Int) {
                        // Do nothing
                    }
                })
        helper.attachToRecyclerView(places_list)

        // Back button callback
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
        viewModel!!.getTour(tourId).observe(this, Observer { tour: Tour? -> setTour(tour) })
        add_place_button!!.setOnClickListener { v: View? ->
            val c = Bundle()
            c.putInt(CreatePlaceFragment.Companion.PLACE_POSITION, viewModel.getPlaces().size)
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .navigate(R.id.toCreatePlace, c)
        }
        val finaltourId = tourId
        edit_button!!.setOnClickListener { v: View? ->
            val c = Bundle()
            c.putInt(CreateTourFragment.Companion.TOUR_ID, finaltourId)
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .navigate(R.id.toEditTour, c)
        }
        create_tour_button!!.setOnClickListener(this)
    }

    private fun setTour(tour: Tour?) {
        if (tour == null) {
            viewModel.getError()
            return
        }
        adapter!!.setPlaces(requireActivity(), tour.places)
        if (tour.id != 0) {
            create_tour_button!!.setText(R.string.update_tour)
        }
        if (tour.name!!.isEmpty()) {
            tour_name!!.text = getString(R.string.click_edit)
        } else {
            tour_name!!.text = tour.name
        }
        if (tour.description!!.isEmpty()) {
            tour_description!!.text = resources.getString(R.string.without_description)
        } else {
            tour_description!!.text = tour.description
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel!!.reset()
    }

    override fun onClick(v: View) {
        if (viewModel.getStoredTour().places.size == 0) {
            Toast.makeText(requireContext(), getString(R.string.at_least_one_place),
                    Toast.LENGTH_SHORT).show()
            return
        }
        create_tour_button!!.isEnabled = false
        viewModel!!.createTour().observe(this, Observer { tour: Tour? ->
            create_tour_button!!.isEnabled = true
            if (tour == null) {
                val err = viewModel.getError()
                Toast.makeText(requireActivity(), err.toString(), Toast.LENGTH_LONG).show()
                Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                        .popBackStack(R.id.navTourList, false)
                return@observe
            }
            Toast.makeText(requireActivity(), R.string.tour_created, Toast.LENGTH_SHORT).show()
            val b = Bundle()
            b.putInt(TourFragment.Companion.TOUR_ID, tour.id)
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .navigate(R.id.toNewTour, b)
        })
    }

    companion object {
        const val TOUR_ID = "TOUR_ID"
        private val TAG = CreatorFragment::class.java.simpleName
    }
}