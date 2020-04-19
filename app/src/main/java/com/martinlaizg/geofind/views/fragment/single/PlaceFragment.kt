package com.martinlaizg.geofind.views.fragment.single

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.viewmodel.TourViewModel
import java.util.*

class PlaceFragment : Fragment(), OnMapReadyCallback {

	private var placeName: TextView? = null
	private var placeDescription: TextView? = null
	private var placePosition: TextView? = null
	private var placeImage: ImageView? = null
	private var placeMap: MapView? = null
	private var mapCircle: ImageView? = null

	private var placeId = 0
	private var googleMap: GoogleMap? = null
	private var viewModel: TourViewModel? = null
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_place, container, false)
		placeName = view.findViewById(R.id.place_name)
		placeDescription = view.findViewById(R.id.place_description)
		placePosition = view.findViewById(R.id.place_position)
		placeImage = view.findViewById(R.id.place_image)
		placeMap = view.findViewById(R.id.place_map)
		mapCircle = view.findViewById(R.id.map_circle)

		placeDescription!!.movementMethod = ScrollingMovementMethod()
		placeMap!!.onCreate(savedInstanceState)
		placeMap!!.onResume()
		placeMap!!.getMapAsync(this)
		val b = arguments
		if (b != null) {
			placeId = b.getInt(PLACE_ID)
		}
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel::class.java)
		viewModel!!.getPlace(placeId).observe(viewLifecycleOwner, Observer { place: Place? -> setPlace(place) })
	}

	private fun setPlace(place: Place?) {
		if (place != null) {
			placeName!!.text = place.name
			placeDescription!!.text = place.description
			if (place.image != null && place.image!!.isNotEmpty()) {
				TODO("load image")
			}
			val number = place.order!! + 1
			val total = viewModel!!.places.size
			placePosition!!.text = resources
					.getQuantityString(R.plurals.place_number_number, number,
							number, total)
			if (googleMap != null) {
				var position = place.position
				val latRand = (Random().nextInt(20000) - 10000) / 3000000f
				val lonRand = (Random().nextInt(20000) - 10000) / 3000000f
				position = LatLng(position!!.latitude + latRand, position.longitude + lonRand)
				val cu = CameraUpdateFactory.newLatLngZoom(position, MAP_ZOOM)
				googleMap!!.moveCamera(cu)
			}
		}
	}

	override fun onMapReady(googleMap: GoogleMap) {
		mapCircle!!.visibility = View.VISIBLE
		googleMap.uiSettings.setAllGesturesEnabled(false)
		googleMap.uiSettings.isMyLocationButtonEnabled = false
		googleMap.uiSettings.isMapToolbarEnabled = false
		this.googleMap = googleMap
	}

	companion object {
		const val PLACE_ID = "PLACE_ID"
		private const val MAP_ZOOM = 14.5f
	}
}