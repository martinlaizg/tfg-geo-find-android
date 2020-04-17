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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.data.access.database.entities.Place
import com.martinlaizg.geofind.views.viewmodel.TourViewModel
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*

class PlaceFragment : Fragment(), OnMapReadyCallback {
    @kotlin.jvm.JvmField
    @BindView(R.id.place_name)
    var place_name: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.place_description)
    var place_description: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.place_position)
    var place_position: TextView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.place_image)
    var place_image: ImageView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.place_map)
    var place_map: MapView? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.map_circle)
    var map_circle: ImageView? = null
    private var place_id = 0
    private var googleMap: GoogleMap? = null
    private var viewModel: TourViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_place, container, false)
        ButterKnife.bind(this, view)
        place_description!!.movementMethod = ScrollingMovementMethod()
        place_map!!.onCreate(savedInstanceState)
        place_map!!.onResume()
        place_map!!.getMapAsync(this)
        val b = arguments
        if (b != null) {
            place_id = b.getInt(PLACE_ID)
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(TourViewModel::class.java)
        viewModel!!.getPlace(place_id).observe(this, Observer { place: Place? -> setPlace(place) })
    }

    private fun setPlace(place: Place?) {
        if (place != null) {
            place_name!!.text = place.name
            place_description!!.text = place.description
            if (place.image != null && !place.image.isEmpty()) {
                Picasso.with(requireContext()).load(place.image)
                        .into(place_image, object : Callback {
                            override fun onSuccess() {
                                place_image!!.visibility = View.VISIBLE
                            }

                            fun onError() {
                                place_image!!.visibility = View.GONE
                            }
                        })
            }
            val number = place.order + 1
            val total = viewModel.getPlaces().size
            place_position!!.text = resources
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
        map_circle!!.visibility = View.VISIBLE
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