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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.card.MaterialCardView
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Play
import com.martinlaizg.geofind.views.adapter.PlayListAdapter.PlaysViewHolder
import com.martinlaizg.geofind.views.fragment.single.TourFragment
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class PlayListAdapter : RecyclerView.Adapter<PlaysViewHolder>() {
    private var plays: List<Play?> = ArrayList()
    private var context: Context? = null
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): PlaysViewHolder {
        context = viewGroup.context
        val view = LayoutInflater.from(viewGroup.context)
                .inflate(R.layout.fragment_play_item, viewGroup, false)
        return PlaysViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaysViewHolder, i: Int) {
        val play = plays[i]
        val t = play.getTour()
        holder.tour_name!!.text = t!!.name
        holder.tour_creator.setText(t.creator.username)
        holder.tour_description!!.text = t!!.description
        if (t.image != null && !t.image.isEmpty()) {
            Picasso.with(context).load(t.image).into(holder.tour_image, object : Callback {
                override fun onSuccess() {
                    holder.tour_image!!.visibility = View.VISIBLE
                }

                fun onError() {
                    holder.tour_image!!.visibility = View.GONE
                }
            })
        }
        val completed = play.getPlaces().size
        val numPlaces = t.places.size
        val progress = completed / numPlaces.toFloat() * 100
        holder.tour_progress!!.setProgress(progress.toInt(), true)
        holder.tour_progress_text!!.text = context!!.getString(R.string.div, completed, numPlaces)
        val b = Bundle()
        b.putInt(TourFragment.Companion.TOUR_ID, t.id)
        holder.tour_card
                .setOnClickListener(View.OnClickListener { v: View? -> Navigation.findNavController(v!!).navigate(R.id.toTour, b) })
    }

    override fun getItemCount(): Int {
        return plays.size
    }

    fun setPlays(plays: List<Play?>) {
        this.plays = plays
        notifyDataSetChanged()
    }

    inner class PlaysViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        @kotlin.jvm.JvmField
        @BindView(R.id.tour_image)
        var tour_image: ImageView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_name)
        var tour_name: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_creator)
        var tour_creator: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_description)
        var tour_description: TextView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_card)
        var tour_card: MaterialCardView? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_progress)
        var tour_progress: ProgressBar? = null

        @kotlin.jvm.JvmField
        @BindView(R.id.tour_progress_text)
        var tour_progress_text: TextView? = null

        init {
            ButterKnife.bind(this, itemView)
        }
    }
}