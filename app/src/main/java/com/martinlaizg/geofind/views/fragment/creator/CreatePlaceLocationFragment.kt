package com.martinlaizg.geofind.views.fragment.creator

import android.Manifest.permission
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.button.MaterialButton
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.views.viewmodel.CreatorViewModel

class CreatePlaceLocationFragment : Fragment(), OnMapReadyCallback, OnMapLongClickListener {

	private var mapView: MapView? = null
	private var createPlaceLocationButton: MaterialButton? = null

	private var viewModel: CreatorViewModel? = null
	private var googleMap: GoogleMap? = null
	private var locationManager: LocationManager? = null
	private var usrLocation: Location? = null
	private var placeLocation: Location? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_create_place_location, container, false)
		mapView = view.findViewById(R.id.map_view)
		createPlaceLocationButton = view.findViewById(R.id.create_place_location_button)
		mapView!!.onCreate(savedInstanceState)
		mapView!!.onResume()
		mapView!!.getMapAsync(this)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		viewModel = ViewModelProviders.of(requireActivity()).get(CreatorViewModel::class.java)
		placeLocation = viewModel!!.place!!.location
		setMarker()
		createPlaceLocationButton!!.setOnClickListener {
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack()
		}
	}

	override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>,
	                                        grantResults: IntArray) {
		if (requestCode == RC_LOCATION && grantResults.isNotEmpty()
				&& grantResults[0] == PackageManager.PERMISSION_GRANTED && googleMap != null) {
			setMarker()
		}
	}

	private fun setMarker() {
		if (googleMap == null) return
		val latLng: LatLng
		googleMap!!.clear()
		if (placeLocation != null) {
			// Use the place location
			latLng = LatLng(placeLocation!!.latitude, placeLocation!!.longitude)
			googleMap!!.addMarker(MarkerOptions().position(latLng))
		} else {
			// Use the user location
			if (usrLocation == null) {
				// Check location permissions
				if (requireActivity()
								.checkSelfPermission(permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
						&& requireActivity().checkSelfPermission(permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
					// Request location permissions
					requireActivity().requestPermissions(arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION), RC_LOCATION)
					return
				}
				locationManager = requireActivity()
						.getSystemService(Context.LOCATION_SERVICE) as LocationManager
				if (locationManager != null) {
					usrLocation = locationManager!!
							.getLastKnownLocation(LocationManager.GPS_PROVIDER)
				}
				if (usrLocation == null) {
					Log.e(TAG, "setMarker: fail to get last known location (GPS)")
					return
				}
			}
			latLng = LatLng(usrLocation!!.latitude, usrLocation!!.longitude)
		}
		googleMap!!.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, ZOOM))
	}

	override fun onResume() {
		super.onResume()
		setMarker()
	}

	override fun onMapReady(googleMap: GoogleMap) {
		this.googleMap = googleMap
		this.googleMap!!.setPadding(0, 0, 0, createPlaceLocationButton!!.height)
		this.googleMap!!.setOnMapLongClickListener(this)
		this.googleMap!!.uiSettings.setAllGesturesEnabled(true)
		setMarker()
		// Check location permissions
		if (requireActivity().checkSelfPermission(permission.ACCESS_FINE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED &&
				requireActivity().checkSelfPermission(permission.ACCESS_COARSE_LOCATION) !=
				PackageManager.PERMISSION_GRANTED) {
			// Request location permissions
			requireActivity()
					.requestPermissions(arrayOf(permission.ACCESS_FINE_LOCATION, permission.ACCESS_COARSE_LOCATION),
							RC_LOCATION)
			return
		}
		this.googleMap!!.isMyLocationEnabled = true
		this.googleMap!!.uiSettings.isMyLocationButtonEnabled = true
	}

	override fun onMapLongClick(latLng: LatLng) {
		googleMap!!.clear()
		googleMap!!.addMarker(MarkerOptions().position(latLng))
		viewModel!!.place!!.position = latLng
	}

	companion object {
		private val TAG = CreatePlaceLocationFragment::class.java.simpleName
		private const val ZOOM = 15f
		private const val RC_LOCATION = 123
	}
}