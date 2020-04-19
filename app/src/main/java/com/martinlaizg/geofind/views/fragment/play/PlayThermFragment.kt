package com.martinlaizg.geofind.views.fragment.play

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.martinlaizg.geofind.R

class PlayThermFragment : PlayTourFragment() {

	private var thermCard: CardView? = null
	private var lastDistance = 0f

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_play_therm, container, false)
		thermCard = view.findViewById(R.id.therm_card)
		return view
	}

	override fun updateView() {
		if (distance > lastDistance) {
			thermCard!!.setCardBackgroundColor(
					resources.getColor(android.R.color.holo_red_dark, null))
		} else {
			thermCard!!.setCardBackgroundColor(
					resources.getColor(android.R.color.holo_green_dark, null))
		}
		lastDistance = distance
	}
}