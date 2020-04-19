package com.martinlaizg.geofind.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.views.adapter.PlayListAdapter
import com.martinlaizg.geofind.views.viewmodel.MainFragmentViewModel

class MainFragment : Fragment() {

	private var toursInProgressView: RecyclerView? = null
	private var toursCompletedText: TextView? = null
	private var toursInProgressText: TextView? = null
	private var toursCompletedCard: CardView? = null

	private var user: User? = null
	private var adapter: PlayListAdapter? = null
	private var viewModel: MainFragmentViewModel? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_main, container, false)
		toursInProgressView = view.findViewById(R.id.toursInProgressView)
		toursCompletedText = view.findViewById(R.id.toursCompletedText)
		toursInProgressText = view.findViewById(R.id.toursInProgressText)
		toursCompletedCard = view.findViewById(R.id.toursCompletedCard)
		val sp = PreferenceManager.getDefaultSharedPreferences(requireActivity())
		user = Preferences.getLoggedUser(sp)
		if (user == null) {
			// If user is not logged, go to LoginFragment
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toLogin)
			return null
		}
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		toursInProgressView!!.layoutManager = LinearLayoutManager(requireActivity())
		adapter = PlayListAdapter()
		toursInProgressView!!.adapter = adapter
		viewModel = ViewModelProviders.of(this).get(MainFragmentViewModel::class.java)
		viewModel!!.getUserPlays(user!!.id)
				.observe(viewLifecycleOwner, Observer { plays: List<Play> -> setPlays(plays) })
	}

	private fun setPlays(plays: List<Play>) {
		toursCompletedCard!!.visibility = View.VISIBLE
		adapter!!.plays = plays
		var inProgress = 0
		var completed = 0
		for (p in plays) {
			if (p.isCompleted) {
				completed++
			} else {
				inProgress++
			}
		}
		toursCompletedText!!.text = resources
				.getQuantityString(R.plurals.you_complete_num_places,
						completed, completed)
		toursInProgressText!!.text = resources.getQuantityString(R.plurals.you_progress_tours,
				inProgress, inProgress)
	}
}