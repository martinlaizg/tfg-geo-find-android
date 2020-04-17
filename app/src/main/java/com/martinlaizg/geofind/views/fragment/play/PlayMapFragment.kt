package com.martinlaizg.geofind.views.fragment.play

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.martinlaizg.geofind.R

class PlayMapFragment : PlayTourFragment(), OnMapReadyCallback {
    @kotlin.jvm.JvmField
    @BindView(R.id.map_type_button)
    var map_type_button: MaterialButton? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.map_view)
    var map_view: MapView? = null
    private var googleMap: GoogleMap? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_play_map, container, false)
        ButterKnife.bind(this, view)
        map_view!!.onCreate(savedInstanceState)
        map_view!!.onResume()
        map_view!!.getMapAsync(this)
        val options = resources.getStringArray(R.array.map_types)
        val mapTypes = intArrayOf(GoogleMap.MAP_TYPE_NORMAL, GoogleMap.MAP_TYPE_SATELLITE)
        map_type_button!!.setOnClickListener { v: View? ->
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle(resources.getString(R.string.map_type))
            builder.setItems(options) { dialog: DialogInterface?, item: Int -> googleMap!!.mapType = mapTypes[item] }
            val alert = builder.create()
            alert.show()
        }
        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        this.googleMap = googleMap
        updateView()
        this.googleMap!!.uiSettings.isMyLocationButtonEnabled = false
        this.googleMap!!.uiSettings.isMapToolbarEnabled = false
    }

    override fun getTag(): String {
        return TAG
    }

    public override fun updateView() {
        Log.d(TAG, "updateView: ")
        if (googleMap != null && usrLocation != null) {
            googleMap!!.isMyLocationEnabled = true
            val cu: CameraUpdate
            val builder = LatLngBounds.Builder()
            builder.include(LatLng(usrLocation!!.latitude, usrLocation!!.longitude))
            if (place != null) {
                // Add place to camera update
                builder.include(place.position)

                // Add place marker
                googleMap!!.clear()
                googleMap!!.addMarker(MarkerOptions().position(place.position))
            }
            val cameraPosition = builder.build()
            cu = if (place == null || distance < DISTANCE_TO_FIX_ZOOM) {
                CameraUpdateFactory.newLatLngZoom(cameraPosition.center, MAX_ZOOM)
            } else {
                CameraUpdateFactory.newLatLngBounds(cameraPosition, MAP_PADDING)
            }
            googleMap!!.uiSettings.isMyLocationButtonEnabled = true
            googleMap!!.animateCamera(cu)
            Log.d(TAG, "updateView: zoom=" + googleMap!!.cameraPosition.zoom)
        }
    }

    companion object {
        private val TAG = PlayMapFragment::class.java.simpleName
        private const val MAX_ZOOM = 18.6f
        private const val DISTANCE_TO_FIX_ZOOM = 100f
        private const val MAP_PADDING = 200
    }
}