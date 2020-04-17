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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.card.MaterialCardView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Tour
import com.martinlaizg.geofind.views.adapter.TourListAdapter.ToursViewHolder
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class TourListAdapter : RecyclerView.Adapter<ToursViewHolder>(), Filterable {
    private var tours: MutableList<Tour?> = ArrayList()
    private var allTours: List<Tour?> = ArrayList()
    private var context: Context? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ToursViewHolder {
        context = viewGroup.context
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.fragment_tour_item, viewGroup, false)
        return ToursViewHolder(view)
    }

    override fun onBindViewHolder(holder: ToursViewHolder, i: Int) {
        val tour = tours[i]
        holder.tourName!!.text = tour!!.name
        holder.tourCreator.setText(tour.creator.username)
        holder.tourDescription!!.text = tour.description
        if (tour.image != null && !tour.image.isEmpty()) {
            Picasso.with(context).load(tour.image).into(holder.tour_image, object : Callback {
                override fun onSuccess() {
                    holder.tour_image!!.visibility = View.VISIBLE
                }

                fun onError() {
                    holder.tour_image!!.visibility = View.GONE
                }
            })
        } else {
            holder.tour_image!!.visibility = View.GONE
        }
        val b = Bundle()
        b.putInt(TourFragment.Companion.TOUR_ID, tours[i].getId())
        holder.materialCardView
                .setOnClickListener(View.OnClickListener { v: View? -> Navigation.findNavController(v!!).navigate(R.id.toTour, b) })
    }

    override fun getItemCount(): Int {
        return tours.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val filtered: MutableList<Tour?> = ArrayList()
                if (charSequence == null || charSequence.length == 0) {
                    filtered.addAll(allTours)
                } else {
                    val filter = charSequence.toString().toLowerCase().trim { it <= ' ' }
                    for (t in allTours) {
                        if (t!!.name!!.toLowerCase().contains(filter) ||
                                t.description!!.toLowerCase().contains(filter)) {
                            filtered.add(t)
                        }
                    }
                }
                val results = FilterResults()
                results.values = filtered
                return results
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                tours.clear()
                tours.addAll(filterResults.values as List<*>)
                notifyDataSetChanged()
            }
        }
    }

    fun getTours(): List<Tour?> {
        return tours
    }

    fun setTours(tours: MutableList<Tour?>) {
        this.tours = tours
        allTours = ArrayList(tours)
        notifyDataSetChanged()
    }

    inner class ToursViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @kotlin.jvm.JvmField
        @BindView(R.id.tour_image)
        var tour_image: ImageView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_name)
        var tourName: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_creator)
        var tourCreator: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_description)
        var tourDescription: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_card)
        var materialCardView: MaterialCardView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}