package com.martinlaizg.geofind.views.fragment.list

import android.os.Bundle
import android.util.Log
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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.card.MaterialCardView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.data.enums.UserType
import com.martinlaizg.geofind.views.adapter.TourListAdapter
import com.martinlaizg.geofind.views.viewmodel.TourListViewModel
import java.util.*

class TourListFragment : Fragment() {
	@kotlin.jvm.JvmField
	@BindView(R.id.tour_list)
	var tour_list: RecyclerView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.create_tour_button)
	var create_tour_button: FloatingActionButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.swipe_refresh)
	var swipe_refresh: SwipeRefreshLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.no_tours_card)
	var no_tours_card: MaterialCardView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.no_tours_card_text)
	var no_tours_card_text: TextView? = null
	private var viewModel: TourListViewModel? = null
	private var adapter: TourListAdapter? = null
	private var stringQuery: String? = null
	override fun onCreateView(inflater: LayoutInflater,
	                          container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_tour_list, container, false)
		ButterKnife.bind(this, view)
		tour_list!!.layoutManager = LinearLayoutManager(requireActivity())
		adapter = TourListAdapter()
		tour_list!!.adapter = adapter
		val arguments = arguments
		if (arguments != null) {
			stringQuery = arguments.getString(SEARCH_QUERY, "")
			stringQuery = stringQuery.trim { it <= ' ' }
			if (!stringQuery!!.isEmpty()) { // With search
				swipe_refresh!!.isEnabled = false
			} else {
				stringQuery = null
				// Set that this fragment has options in toolbar
				setHasOptionsMenu(true)
			}
		}
		requireActivity().onBackPressedDispatcher
				.addCallback(this, object : OnBackPressedCallback(true) {
					override fun handleOnBackPressed() {
						Objects.requireNonNull(
								(requireActivity() as AppCompatActivity).supportActionBar)
								.setSubtitle(null)
					}
				})
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(this).get(TourListViewModel::class.java)
		swipe_refresh!!.isRefreshing = true
		viewModel!!.getTours(stringQuery).observe(this, Observer { tours: MutableList<Tour?>? -> setTours(tours) })
		val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
		val u = Preferences.getLoggedUser(sp)
		if (u.user_type != null && u.user_type != UserType.USER) {
			create_tour_button!!.setOnClickListener(
					Navigation.createNavigateOnClickListener(R.id.toCreateTour))
		} else {
			create_tour_button!!.setOnClickListener { v: View? ->
				Toast
						.makeText(requireContext(), getString(R.string.no_permissions),
								Toast.LENGTH_SHORT).show()
			}
		}
		swipe_refresh!!.setOnRefreshListener { viewModel!!.getTours(stringQuery).observe(this, Observer { tours: MutableList<Tour?>? -> setTours(tours) }) }
	}

	private fun setTours(tours: MutableList<Tour?>?) {
		swipe_refresh!!.isRefreshing = false
		if (tours != null) {
			if (tours.isEmpty()) {
				swipe_refresh!!.visibility = View.GONE
				no_tours_card!!.visibility = View.VISIBLE
				no_tours_card_text!!.text = getString(R.string.no_tours_match)
				return
			}
			adapter!!.tours = tours
		} else {
			val error = viewModel.getError()
			if (error == ErrorType.NETWORK) {
				Toast.makeText(requireContext(), resources.getString(R.string.network_error),
						Toast.LENGTH_SHORT).show()
			} else {
				Log.e(TAG, "onCreateView: $error")
			}
		}
	}

	override fun onStart() {
		Objects.requireNonNull((requireActivity() as AppCompatActivity).supportActionBar)
				.setSubtitle(stringQuery)
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
			swipe_refresh!!.isEnabled = false
			return true
		}
		return super.onOptionsItemSelected(item)
	}

	companion object {
		private const val SEARCH_QUERY = "SEARCH_QUERY"
		private val TAG = TourListFragment::class.java.simpleName
	}
}