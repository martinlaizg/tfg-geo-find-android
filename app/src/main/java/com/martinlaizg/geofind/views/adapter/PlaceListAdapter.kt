package com.martinlaizg.geofind.views.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.adapter.PlaceListAdapter.PlaceViewHolder
import com.martinlaizg.geofind.views.fragment.single.PlaceFragment
import java.util.*

class PlaceListAdapter(private val completed: Boolean) : RecyclerView.Adapter<PlaceViewHolder>() {
    private var places: List<Place?>
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.fragment_place_item, viewGroup, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val l = places[position]
        holder.place_name!!.text = l!!.name
        holder.place_description!!.text = l.description
        val b = Bundle()
        b.putInt(PlaceFragment.Companion.PLACE_ID, l.id)
        holder.place_card
                .setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toPlace, b))
        if (completed) {
            holder.completePlace()
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }

    fun setPlaces(places: List<Place?>) {
        this.places = places
        notifyDataSetChanged()
    }

    inner class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val completedColor: Int

        @kotlin.jvm.JvmField
        @BindView(R.id.place_name)
        var place_name: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.place_description)
        var place_description: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.place_card)
        var place_card: CardView? = null
        fun completePlace() {
            place_name!!.setTextColor(completedColor)
            place_description!!.setTextColor(completedColor)
        }

        init {
            completedColor = itemView.context.resources
                    .getColor(R.color.colorPrimaryLightAlpha, null)
            ButterKnife.bind(this, itemView)
        }
    }

    init {
        places = ArrayList()
    }
}