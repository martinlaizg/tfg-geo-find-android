package com.martinlaizg.geofind.views.fragment.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.Secure
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel

class RegistryFragment : Fragment() {

	private var emailInput: TextInputLayout? = null
	private var passwordInput: TextInputLayout? = null
	private var cPasswordInput: TextInputLayout? = null
	private var btnRegistry: MaterialButton? = null

	private var viewModel: LoginViewModel? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_registry, container, false)
		emailInput = view.findViewById(R.id.email_input)
		passwordInput = view.findViewById(R.id.password_input)
		cPasswordInput = view.findViewById(R.id.c_password_input)
		btnRegistry = view.findViewById(R.id.btn_registry)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)
		btnRegistry!!.setOnClickListener { registry() }
	}

	private fun registry() {
		val email = emailInput!!.editText!!.text.toString().trim()
		val password = passwordInput!!.editText!!.text.toString().trim()
		val cPassword = cPasswordInput!!.editText!!.text.toString().trim()
		btnRegistry!!.isEnabled = false
		if (email.isEmpty()) {
			emailInput!!.error = getString(R.string.required_password)
			return
		}
		if (password.isEmpty()) {
			passwordInput!!.error = getString(R.string.required_verify_password)
			return
		}
		if (cPassword.isEmpty()) {
			cPasswordInput!!.error = getString(R.string.required_verify_password)
			return
		}
		if (cPassword != password) {
			cPasswordInput!!.error = getString(R.string.password_does_not_match)
			return
		}
		val l = Login(email, Secure.hash(password))
		viewModel!!.registry(l).observe(viewLifecycleOwner, Observer { user: User? ->
			btnRegistry!!.isEnabled = true
			if (user == null) {
				if (viewModel!!.error == ErrorType.EMAIL) {
					emailInput!!.error = getString(R.string.email_in_use)
				} else {
					Toast.makeText(requireContext(), getString(R.string.other_error),
							Toast.LENGTH_SHORT).show()
				}
				return@Observer
			}
			val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
			Preferences.setLoggedUser(sp, user)
			Preferences.setLogin(sp, l)
			Toast.makeText(requireActivity(), getString(R.string.registered), Toast.LENGTH_LONG).show()
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder).popBackStack()
		})
	}
}