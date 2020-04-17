package com.martinlaizg.geofind.views.preferences

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.views.viewmodel.SettingsViewModel
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
	private var dialog: AlertDialog? = null
	private var viewModel: SettingsViewModel? = null
	private var mGoogleSignInClient: GoogleSignInClient? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity()).get(SettingsViewModel::class.java)
	}

	override fun onCreatePreferences(savedInstanceState: Bundle, rootKey: String) {
		addPreferencesFromResource(R.xml.app_preferences)
		requireActivity().setTheme(R.style.AppTheme_ScreenPreferences)
		createSupportMessageDialog()
		var preference = findPreference<Preference>(getString(R.string.pref_log_out))
		if (preference != null) preference.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> onLogOut() }
		preference = findPreference(getString(R.string.pref_support))
		if (preference != null) preference.onPreferenceClickListener = Preference.OnPreferenceClickListener { preference: Preference -> onSupport() }
	}

	private fun createSupportMessageDialog() {
		val builder = AlertDialog.Builder(requireContext())
		val view = layoutInflater
				.inflate(R.layout.support_message, LinearLayout(requireContext()), false)
		val titleLayout: TextInputLayout = view.findViewById(R.id.title_layout)
		val messageTextLayout: TextInputLayout = view.findViewById(R.id.message_text_layout)
		val sendButton: MaterialButton = view.findViewById(R.id.send_button)
		sendButton.setOnClickListener { v: View? ->
			if (Objects.requireNonNull(titleLayout.editText).text.toString().trim { it <= ' ' }
							.isEmpty()) {
				titleLayout.error = "You should fill it"
				return@setOnClickListener
			}
			if (Objects.requireNonNull(messageTextLayout.editText).text.toString().trim { it <= ' ' }
							.isEmpty()) {
				messageTextLayout.error = "You should fill it"
				return@setOnClickListener
			}
			val title = titleLayout.editText!!.text.toString()
			val message = messageTextLayout.editText!!.text.toString()
			viewModel!!.sendMessage(title, message).observe(this, Observer { ok: Boolean? ->
				if (ok == null) {
					val e = viewModel.getError()
					Log.e(TAG, "setLogoutPreference: $e")
				} else if (ok) {
					Toast.makeText(requireContext(), "Message sent", Toast.LENGTH_SHORT).show()
					Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
							.popBackStack()
					dialog!!.dismiss()
				} else {
					Toast.makeText(requireContext(), "Message no sent", Toast.LENGTH_SHORT).show()
				}
			})
		}
		builder.setView(view)
		dialog = builder.create()
	}

	private fun onLogOut(): Boolean {
		mGoogleSignInClient!!.signOut().addOnCompleteListener(requireActivity()) { task: Task<Void?>? ->
			val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
			Preferences.logout(sp)
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.navigate(R.id.toMain)
		}
		return true
	}

	private fun onSupport(): Boolean {
		dialog!!.show()
		return true
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val gso = GoogleSignInOptions.Builder(
				GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(resources.getString(R.string.client_id)).requestEmail()
				.build()
		mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		return super.onCreateView(inflater, container, savedInstanceState)
	}

	companion object {
		private val TAG = SettingsFragment::class.java.simpleName
	}
}