package com.martinlaizg.geofind.views.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.views.adapter.TourListAdapter.ToursViewHolder
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import java.util.*
import kotlin.collections.ArrayList

class TourListAdapter : RecyclerView.Adapter<ToursViewHolder>(), Filterable {

	var tours: List<Tour> = ArrayList()
		set(value) {
			notifyDataSetChanged()
			field = value
		}

	private var context: Context? = null

	override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ToursViewHolder {
		context = viewGroup.context
		val view = LayoutInflater.from(viewGroup.context)
				.inflate(R.layout.fragment_tour_item, viewGroup, false)
		return ToursViewHolder(view)
	}

	override fun onBindViewHolder(holder: ToursViewHolder, i: Int) = holder.bind(tours[i])

	override fun getItemCount(): Int {
		return tours.size
	}

	override fun getFilter(): Filter {
		return object : Filter() {
			override fun performFiltering(charSequence: CharSequence): FilterResults {
				val stringFilter: String = charSequence.toString().toLowerCase(Locale.getDefault()).trim()
				val filtered: MutableList<Tour?> = ArrayList()
				if (stringFilter.isEmpty()) {
					filtered.addAll(tours)
				} else {
					for (t in tours) {
						if (t.name.toLowerCase(Locale.getDefault()).contains(stringFilter) ||
								t.description.toLowerCase(Locale.getDefault()).contains(stringFilter)) {
							filtered.add(t)
						}
					}
				}
				val results = FilterResults()
				results.values = filtered
				return results
			}

			@Suppress("UNCHECKED_CAST")
			override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
				tours = filterResults.values as List<Tour>
				notifyDataSetChanged()
			}
		}
	}

	inner class ToursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

		private var tourImage: ImageView = itemView.findViewById(R.id.tour_image)
		private var tourName: TextView = itemView.findViewById(R.id.tour_name)
		private var tourCreator: TextView = itemView.findViewById(R.id.tour_creator)
		private var tourDescription: TextView = itemView.findViewById(R.id.tour_description)
		private var materialCardView: MaterialCardView = itemView.findViewById(R.id.tour_card)

		fun bind(tour: Tour) {
			tourName.text = tour.name
			tourCreator.text = tour.creator!!.username
			tourDescription.text = tour.description
			if (tour.image != null && tour.image!!.isNotEmpty()) {
				TODO("load image")
			} else {
				tourImage.visibility = View.GONE
			}
			val b = Bundle()
			b.putInt(TourFragment.Companion.TOUR_ID, tour.id)
			materialCardView.setOnClickListener { v: View? -> Navigation.findNavController(v!!).navigate(R.id.toTour, b) }
		}
	}
}