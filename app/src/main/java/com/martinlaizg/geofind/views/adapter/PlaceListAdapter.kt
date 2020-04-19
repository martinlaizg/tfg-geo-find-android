package com.martinlaizg.geofind.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.adapter.PlaceListAdapter.PlaceViewHolder
import com.martinlaizg.geofind.views.fragment.single.PlaceFragment

class PlaceListAdapter(private val completed: Boolean) : RecyclerView.Adapter<PlaceViewHolder>() {

	var places: List<Place> = ArrayList()
		set(value) {
			notifyDataSetChanged()
			field = value
		}

	override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlaceViewHolder {
		val view = LayoutInflater.from(viewGroup.context)
				.inflate(R.layout.fragment_place_item, viewGroup, false)
		return PlaceViewHolder(view)
	}

	override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) = holder.bind(places[position])

	override fun getItemCount(): Int {
		return places.size
	}

	inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
		private val completedColor: Int = itemView.context.resources.getColor(R.color.colorPrimaryLightAlpha, null)
		var placeName: TextView = itemView.findViewById(R.id.place_name)
		var placeDescription: TextView = itemView.findViewById(R.id.place_description)
		var placeCard: CardView = itemView.findViewById(R.id.place_card)

		private fun completePlace() {
			placeName.setTextColor(completedColor)
			placeDescription.setTextColor(completedColor)
		}

		fun bind(place: Place) {
			placeName.text = place.name
			placeDescription.text = place.description
			val b = Bundle()
			b.putInt(PlaceFragment.Companion.PLACE_ID, place.id)
			placeCard.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlace, b))
			if (completed) {
				completePlace()
			}
		}
	}

}