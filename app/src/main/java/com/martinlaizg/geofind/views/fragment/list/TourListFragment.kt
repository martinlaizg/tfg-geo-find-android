package com.martinlaizg.geofind.views.fragment.list

import android.os.Bundle
import android.view.*
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.enums.UserType
import com.martinlaizg.geofind.views.adapter.TourListAdapter
import com.martinlaizg.geofind.views.viewmodel.TourListViewModel

class TourListFragment : Fragment() {

	private var tourList: RecyclerView? = null
	private var createTourButton: FloatingActionButton? = null
	private var swipeRefresh: SwipeRefreshLayout? = null
	private var noToursCard: MaterialCardView? = null
	private var noToursCardText: TextView? = null

	private var viewModel: TourListViewModel? = null
	private var adapter: TourListAdapter? = null
	private var stringQuery: String? = null

	override fun onCreateView(inflater: LayoutInflater,
	                          container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_tour_list, container, false)
		tourList = view.findViewById(R.id.tour_list)
		createTourButton = view.findViewById(R.id.create_tour_button)
		swipeRefresh = view.findViewById(R.id.swipe_refresh)
		noToursCard = view.findViewById(R.id.no_tours_card)
		noToursCardText = view.findViewById(R.id.no_tours_card_text)

		tourList!!.layoutManager = LinearLayoutManager(requireActivity())
		adapter = TourListAdapter()
		tourList!!.adapter = adapter
		val arguments = arguments
		if (arguments != null) {
			stringQuery = arguments.getString(SEARCH_QUERY, "").trim()
			if (stringQuery!!.isNotEmpty()) {
				swipeRefresh!!.isEnabled = false
			} else {
				stringQuery = null
				// Set that this fragment has options in toolbar
				setHasOptionsMenu(true)
			}
		}
		requireActivity().onBackPressedDispatcher
				.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
					override fun handleOnBackPressed() {
						(requireActivity() as AppCompatActivity).supportActionBar!!.subtitle = null
					}
				})
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(this).get(TourListViewModel::class.java)
		swipeRefresh!!.isRefreshing = true
		viewModel!!.getTours(stringQuery ?: "")
				.observe(viewLifecycleOwner, Observer { tours: MutableList<Tour> -> setTours(tours) })
		val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
		val u = Preferences.getLoggedUser(sp)
		if (u.userType != null && u.userType != UserType.USER) {
			createTourButton!!.setOnClickListener(
					Navigation.createNavigateOnClickListener(R.id.toCreateTour))
		} else {
			createTourButton!!.setOnClickListener {
				Toast.makeText(requireContext(), getString(R.string.no_permissions), Toast.LENGTH_SHORT).show()
			}
		}
		swipeRefresh!!.setOnRefreshListener {
			viewModel!!.getTours(stringQuery ?: "")
					.observe(viewLifecycleOwner, Observer { tours: MutableList<Tour> -> setTours(tours) })
		}
	}

	private fun setTours(tours: MutableList<Tour>) {
		swipeRefresh!!.isRefreshing = false
		if (tours.isEmpty()) {
			swipeRefresh!!.visibility = View.GONE
			noToursCard!!.visibility = View.VISIBLE
			noToursCardText!!.text = getString(R.string.no_tours_match)
			return
		}
		adapter!!.tours = tours
	}

	override fun onStart() {
		(requireActivity() as AppCompatActivity).supportActionBar!!.subtitle = stringQuery
		super.onStart()
	}

	override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
		inflater.inflate(R.menu.toolbar_menu, menu)
		val searchView = menu.findItem(R.id.app_bar_search).actionView as SearchView
		searchView.setIconifiedByDefault(false)
		searchView.imeOptions = EditorInfo.IME_ACTION_DONE
		searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
			override fun onQueryTextSubmit(query: String): Boolean {
				val args = Bundle()
				args.putString(SEARCH_QUERY, query)
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.navigate(R.id.searchTours, args)
				return true
			}

			override fun onQueryTextChange(newText: String): Boolean {
				adapter!!.filter.filter(newText)
				return true
			}
		})
		super.onCreateOptionsMenu(menu, inflater)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		if (item.itemId == R.id.app_bar_search) {
			swipeRefresh!!.isEnabled = false
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {
		private const val SEARCH_QUERY = "SEARCH_QUERY"
	}
}