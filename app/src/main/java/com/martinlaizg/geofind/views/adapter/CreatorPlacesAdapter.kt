package com.martinlaizg.geofind.views.adapter

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.adapter.CreatorPlacesAdapter.CreatorPlacesViewHolder
import com.martinlaizg.geofind.views.fragment.creator.CreatePlaceFragment
import java.util.*
import kotlin.collections.ArrayList

class CreatorPlacesAdapter : RecyclerView.Adapter<CreatorPlacesViewHolder>(), ItemTouchHelperAdapter {

	var places: List<Place> = ArrayList()
		set(value) {
			field = value.sortedBy { place -> place.order }
			notifyDataSetChanged()
			notifyDataSetChanged()
		}

	var fragmentActivity: FragmentActivity? = null

	override fun getItemCount(): Int = places.size

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorPlacesViewHolder {
		val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.fragment_editable_place, parent, false)
		return CreatorPlacesViewHolder(view)
	}

	override fun onBindViewHolder(holder: CreatorPlacesViewHolder, position: Int) = holder.bind(places[position], position)

	private fun showExitDialog(context: Context, position: Int) {
		MaterialAlertDialogBuilder(context)
				.setTitle(R.string.are_you_sure)
				.setMessage(context.getString(R.string.permanent_delete))
				.setPositiveButton(context.getString(R.string.ok)) { _: DialogInterface?, _: Int -> remove(position) }
				.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface: DialogInterface, _: Int -> dialogInterface.dismiss() }
				.show()
	}

	private fun remove(position: Int) {
		places = places.drop(position)
		for (i in places.indices) {
			places[i].order = i
		}
		notifyDataSetChanged()
	}

	override fun onItemMove(fromPosition: Int, toPosition: Int) {
		Collections.swap(places, fromPosition, toPosition)
		for (i in places.indices) {
			places[i].order = i
		}
		notifyItemMoved(fromPosition, toPosition)
		notifyItemChanged(fromPosition)
		notifyItemChanged(toPosition)
	}

	inner class CreatorPlacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		private var mPlaceCard: MaterialCardView = view.findViewById(R.id.place_card)
		private var mPlaceDeleteButton: MaterialButton = view.findViewById(R.id.place_delete_button)
		private var mPlaceName: TextView = view.findViewById(R.id.place_name)
		private var mQuestionnaireIcon: ImageView = view.findViewById(R.id.questionnaire_icon)

		fun bind(place: Place, position: Int) {
			mPlaceName.text = place.name
			mPlaceDeleteButton.setOnClickListener { v: View -> showExitDialog(v.context, position) }
			mQuestionnaireIcon.visibility = View.GONE
			if (place.question != null) {
				mQuestionnaireIcon.visibility = View.VISIBLE
			}
			val b = Bundle()
			b.putInt(CreatePlaceFragment.PLACE_POSITION, place.order!!)
			mPlaceCard.setOnClickListener {
				Navigation.findNavController(fragmentActivity!!, R.id.main_fragment_holder)
						.navigate(R.id.toCreatePlace, b)
			}
		}
	}
}