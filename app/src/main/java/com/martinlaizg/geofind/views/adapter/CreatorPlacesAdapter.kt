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

class CreatorPlacesAdapter : RecyclerView.Adapter<CreatorPlacesViewHolder>(), ItemTouchHelperAdapter {

	private var places: List<Place>
	private var fragmentActivity: FragmentActivity? = null

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorPlacesViewHolder {
		val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.fragment_editable_place, parent, false)
		return CreatorPlacesViewHolder(view)
	}

	override fun onBindViewHolder(holder: CreatorPlacesViewHolder, position: Int) = holder.bind(places[position], position)

	override fun getItemCount(): Int {
		return places.size
	}

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

	fun setPlaces(fragmentActivity: FragmentActivity, newPlaces: List<Place>) {
		this.fragmentActivity = fragmentActivity
		places = newPlaces.sortedBy { place -> place.order }
		notifyDataSetChanged()
	}

	inner class CreatorPlacesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
		var mPlaceCard: MaterialCardView
		var mPlaceDeleteButton: MaterialButton
		var mPlaceName: TextView
		var mQuestionnaireIcon: ImageView

		init {
			mPlaceCard = view.findViewById(R.id.place_card)
			mPlaceDeleteButton = view.findViewById(R.id.place_delete_button)
			mPlaceName = view.findViewById(R.id.place_name)
			mQuestionnaireIcon = view.findViewById(R.id.questionnaire_icon)
		}

		fun bind(place: Place, position: Int) {
			mPlaceName.text = place.name
			mPlaceDeleteButton.setOnClickListener { v: View -> showExitDialog(v.context, position) }
			mQuestionnaireIcon.visibility = View.GONE
			if (place.question != null) {
				mQuestionnaireIcon.visibility = View.VISIBLE
			}
			val b = Bundle()
			b.putInt(CreatePlaceFragment.Companion.PLACE_POSITION, place.order!!)
			mPlaceCard.setOnClickListener {
				Navigation.findNavController(fragmentActivity!!, R.id.main_fragment_holder)
						.navigate(R.id.toCreatePlace, b)
			}
		}

	}

	init {
		places = ArrayList()
	}
}