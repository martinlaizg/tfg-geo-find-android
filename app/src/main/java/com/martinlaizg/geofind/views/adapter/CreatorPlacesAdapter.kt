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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.adapter.CreatorPlacesAdapter.CreatorPlacesViewHolder
import com.martinlaizg.geofind.views.fragment.creator.CreatePlaceFragment
import java.util.*

class CreatorPlacesAdapter : RecyclerView.Adapter<CreatorPlacesViewHolder>(), ItemTouchHelperAdapter {
	private var places: List<Place?>
	private var fragmentActivity: FragmentActivity? = null
	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CreatorPlacesViewHolder {
		val view = LayoutInflater.from(parent.context)
				.inflate(R.layout.fragment_editable_place, parent, false)
		return CreatorPlacesViewHolder(view)
	}

	override fun onBindViewHolder(holder: CreatorPlacesViewHolder, position: Int) {
		val place = places[position]
		holder.place_name!!.text = place!!.name
		holder.place_delete_button
				.setOnClickListener(View.OnClickListener { v: View -> showExitDialog(v.context, position) })
		holder.questionnaire_icon!!.visibility = View.GONE
		if (place.question != null) {
			holder.questionnaire_icon!!.visibility = View.VISIBLE
		}
		val b = Bundle()
		b.putInt(CreatePlaceFragment.Companion.PLACE_POSITION, place.order)
		holder.place_card!!.setOnClickListener { v: View? ->
			Navigation.findNavController(fragmentActivity!!, R.id.main_fragment_holder)
					.navigate(R.id.toCreatePlace, b)
		}
	}

	override fun getItemCount(): Int {
		return places.size
	}

	private fun showExitDialog(context: Context, position: Int) {
		MaterialAlertDialogBuilder(context).setTitle(R.string.are_you_sure)
				.setMessage(context.getString(R.string.permanent_delete))
				.setPositiveButton(context.getString(R.string.ok)
				) { dialog: DialogInterface?, which: Int -> remove(position) }
				.setNegativeButton(context.getString(R.string.cancel)
				) { dialogInterface: DialogInterface, i: Int -> dialogInterface.dismiss() }.show()
	}

	private fun remove(position: Int) {
		places.removeAt(position)
		for (i in places.indices) {
			places[i].setOrder(i)
		}
		notifyDataSetChanged()
	}

	override fun onItemMove(from: Int, to: Int) {
		Collections.swap(places, from, to)
		for (i in places.indices) {
			places[i].setOrder(i)
		}
		notifyItemMoved(from, to)
		notifyItemChanged(from)
		notifyItemChanged(to)
	}

	fun setPlaces(fragmentActivity: FragmentActivity?, places: List<Place?>?) {
		this.fragmentActivity = fragmentActivity
		if (places != null) {
			// sort elements
			places.sort(Comparator { o1: Place, o2: Place -> o1.order.compareTo(o2.order) })
			this.places = places
			notifyDataSetChanged()
		}
	}

	inner class CreatorPlacesViewHolder(view: View?) : RecyclerView.ViewHolder(view!!) {
		@kotlin.jvm.JvmField
		@BindView(R.id.place_card)
		var place_card: MaterialCardView? = null

		@kotlin.jvm.JvmField
		@BindView(R.id.place_delete_button)
		var place_delete_button: MaterialButton? = null

		@kotlin.jvm.JvmField
		@BindView(R.id.place_name)
		var place_name: TextView? = null

		@kotlin.jvm.JvmField
		@BindView(R.id.questionnaire_icon)
		var questionnaire_icon: ImageView? = null

		init {
			ButterKnife.bind(this, view!!)
		}
	}

	init {
		places = ArrayList()
	}
}