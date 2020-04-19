package com.martinlaizg.geofind.views.activity

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.NavigationUI
import androidx.preference.PreferenceManager
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.access.api.RetrofitFactory
import com.martinlaizg.geofind.data.repository.UserRepository
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

	private var navController: NavController? = null
	private var appBarConfiguration: AppBarConfiguration? = null
	override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
		Log.i(TAG, "onSharedPreferenceChanged: changed")
		when (key) {
			Preferences.USER -> {
				val u = Preferences.getLoggedUser(sharedPreferences)
				if (u != null) {
					setDrawerHeader(u.username, u.name, u.image)
				}
			}
			Preferences.TOKEN -> {
				val token = Preferences.getToken(sharedPreferences)
				setServicesToken(token)
			}
		}
	}

	private fun setDrawerHeader(username: String?, name: String?, image: String?) {
		var username = username
		var name = name
		val headerView = navigationView!!.getHeaderView(0)
		if (name == null) {
			name = getString(R.string.your_account)
		}
		if (username == null) {
			username = ""
		}
		(headerView.findViewById<View>(R.id.drawer_header_name) as TextView).text = name
		(headerView.findViewById<View>(R.id.drawer_header_username) as TextView).text = username
		if (image != null && image.isNotEmpty()) {
			headerView.findViewById<ImageView>(R.id.drawer_user_image)
		}
	}

	private fun setServicesToken(token: String?) {
		RetrofitFactory.token = token
	}

	override fun onCreate(savedInstanceState: Bundle?,
	                      persistentState: PersistableBundle?) {
		super.onCreate(savedInstanceState, persistentState)
		setContentView(R.layout.activity_main)
		UserRepository.getInstance(application)
	}

	override fun onOptionsItemSelected(item: MenuItem): Boolean {
		return NavigationUI.onNavDestinationSelected(item, navController!!) ||
				super.onOptionsItemSelected(item)
	}

	override fun onBackPressed() {
		if (drawer_layout!!.isDrawerOpen(GravityCompat.START)) {
			drawer_layout!!.closeDrawer(GravityCompat.START)
		} else {
			super.onBackPressed()
		}
	}

	override fun onPause() {
		super.onPause()
		// Unregister the listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.unregisterOnSharedPreferenceChangeListener(this)
	}

	override fun onResume() {
		super.onResume()
		// Set up a listener whenever a key changes
		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this)
	}

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)
		setSupportActionBar(toolbar)

		// Set Navigation Controller
		navController = Navigation.findNavController(this, R.id.main_fragment_holder)

		// Setup the navigation view (drawer)
		NavigationUI.setupWithNavController(navigationView!!, navController!!)

		// Set the toolbar
		val topLevelDestinations: MutableSet<Int> = HashSet()
		topLevelDestinations.add(R.id.navMain)
		topLevelDestinations.add(R.id.navTourList)
		appBarConfiguration = AppBarConfiguration.Builder(topLevelDestinations)
				.setDrawerLayout(drawer_layout).build()
		NavigationUI.setupWithNavController(toolbar!!, navController!!, appBarConfiguration!!)
		navController!!.addOnDestinationChangedListener { navController: NavController, destination: NavDestination, bundle: Bundle? -> onDestinationChangedListener(destination) }
		PreferenceManager.setDefaultValues(this, R.xml.app_preferences, false)
		val sp = PreferenceManager.getDefaultSharedPreferences(this)
		val u = Preferences.getLoggedUser(sp)
		setDrawerHeader(u.username, u.name, u.image)
		val token = Preferences.getToken(sp)
		setServicesToken(token)
	}

	private fun onDestinationChangedListener(destination: NavDestination) {
		if (destination.id == R.id.navLogin || destination.id == R.id.navRegistry) {
			toolbar!!.visibility = View.GONE
			drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
		} else {
			toolbar!!.visibility = View.VISIBLE
			drawer_layout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
		}
	}

	override fun onSupportNavigateUp(): Boolean {
		return NavigationUI.navigateUp(navController!!, appBarConfiguration!!) ||
				super.onSupportNavigateUp()
	}

	companion object {
		private val TAG = MainActivity::class.java.simpleName
	}
}