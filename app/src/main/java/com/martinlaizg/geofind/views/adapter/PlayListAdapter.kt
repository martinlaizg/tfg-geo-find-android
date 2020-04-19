package com.martinlaizg.geofind.views.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.views.adapter.PlayListAdapter.PlaysViewHolder
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import java.util.*

class PlayListAdapter : RecyclerView.Adapter<PlaysViewHolder>() {

	var plays: List<Play> = ArrayList()
		set(value) {
			notifyDataSetChanged()
			field = value
		}

	private var context: Context? = null

	override fun onBindViewHolder(holder: PlaysViewHolder, i: Int) = holder.bind(plays[i])
	override fun getItemCount(): Int = plays.size
	override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PlaysViewHolder {
		context = viewGroup.context
		val view = LayoutInflater.from(viewGroup.context)
				.inflate(R.layout.fragment_play_item, viewGroup, false)
		return PlaysViewHolder(view)
	}

	inner class PlaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		fun bind(play: Play) {
			val t = play.tour
			tourName.text = t!!.name
			tourCreator.text = t.creator!!.username
			tourDescription.text = t.description
			if (t.image != null && t.image!!.isNotEmpty()) {
				TODO("load image")
			}
			val completed = play.places.size
			val numPlaces = t.places.size
			val progress = completed / numPlaces.toFloat() * 100
			tourProgress.setProgress(progress.toInt(), true)
			tourProgressText.text = context!!.getString(R.string.div, completed, numPlaces)
			val b = Bundle()
			b.putInt(TourFragment.TOUR_ID, t.id)
			tourCard.setOnClickListener { v: View? -> Navigation.findNavController(v!!).navigate(R.id.toTour, b) }
		}

		var tourImage: ImageView = itemView.findViewById(R.id.tour_image)
		private var tourName: TextView = itemView.findViewById(R.id.tour_name)
		private var tourCreator: TextView = itemView.findViewById(R.id.tour_creator)
		private var tourDescription: TextView = itemView.findViewById(R.id.tour_description)
		private var tourCard: MaterialCardView = itemView.findViewById(R.id.tour_card)
		private var tourProgress: ProgressBar = itemView.findViewById(R.id.tour_progress)
		private var tourProgressText: TextView = itemView.findViewById(R.id.tour_progress_text)
	}
}