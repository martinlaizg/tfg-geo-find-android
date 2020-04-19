package com.martinlaizg.geofind.views.fragment

import android.content.DialogInterface
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
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
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.views.viewmodel.EditProfileViewModel

class EditProfileFragment : Fragment() {
	private var userImage: ImageView? = null
	private var nameInput: TextInputLayout? = null
	private var usernameInput: TextInputLayout? = null
	private var emailInput: TextInputLayout? = null
	private var changeImageButton: MaterialButton? = null
	private var saveButton: MaterialButton? = null
	private var newPasswordInput: TextInputLayout? = null
	private var cNewPasswordInput: TextInputLayout? = null
	private var changePasswordButton: MaterialButton? = null
	private var passwordLayout: ConstraintLayout? = null
	private var confirmPasswordInput: TextInputLayout? = null

	private var sp: SharedPreferences? = null
	private var user: User? = null
	private var login: Login? = null
	private var viewModel: EditProfileViewModel? = null

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
		userImage = view.findViewById(R.id.user_image)
		nameInput = view.findViewById(R.id.name_input)
		usernameInput = view.findViewById(R.id.username_input)
		emailInput = view.findViewById(R.id.email_input)
		changeImageButton = view.findViewById(R.id.change_image_button)
		saveButton = view.findViewById(R.id.save_button)
		newPasswordInput = view.findViewById(R.id.new_password_input)
		cNewPasswordInput = view.findViewById(R.id.c_new_password_input)
		changePasswordButton = view.findViewById(R.id.change_password_button)
		passwordLayout = view.findViewById(R.id.password_layout)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
		login = Preferences.getLogin(sp)
		if (login!!.provider != Login.Provider.OWN) {
			passwordLayout!!.visibility = View.GONE
			emailInput!!.visibility = View.GONE
		}
		viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)

		// Buttons
		changeImageButton!!.setOnClickListener {
			Toast.makeText(requireContext(), getString(R.string.work_in_progress), Toast.LENGTH_SHORT).show()
		}
		changeImageButton!!.visibility = View.GONE
		saveButton!!.setOnClickListener { saveAction() }
		changePasswordButton!!.setOnClickListener { changePasswordAction() }
	}

	private fun saveAction() {
		val name = nameInput!!.editText!!.text.toString().trim()
		val username = usernameInput!!.editText!!.text.toString().trim()
		val email = emailInput!!.editText!!.text.toString().trim()
		user = Preferences.getLoggedUser(sp)
		user!!.name = name
		user!!.username = username
		login = Preferences.getLogin(sp)
		if (login!!.provider == Login.Provider.OWN) {
			user!!.email = email
			val passADB = passwordDialog
			passADB.setPositiveButton(R.string.save) { dialog: DialogInterface, _: Int ->
				val password = confirmPasswordInput!!.editText!!.text.toString().trim()
				if (login!!.secure == Secure.hash(password)) {
					updateUser()
				} else {
					Toast.makeText(requireContext(), getString(R.string.wrong_password),
							Toast.LENGTH_SHORT).show()
				}
				dialog.dismiss()
			}
			passADB.show()
		} else {
			updateUser()
		}
	}

	private fun changePasswordAction() {
		val newPassword = newPasswordInput!!.editText!!.text.toString().trim()
		val cNewPassword = cNewPasswordInput!!.editText!!.text.toString().trim()
		if (newPassword.isEmpty()) {
			newPasswordInput!!.error = getString(R.string.required_password)
			return
		}
		if (newPassword != cNewPassword) {
			cNewPasswordInput!!.error = getString(R.string.password_does_not_match)
			return
		}
		user = Preferences.getLoggedUser(sp)
		user!!.secure = Secure.hash(newPassword)
		login = Preferences.getLogin(sp)
		if (login!!.provider == Login.Provider.OWN) {
			val passADB = passwordDialog
			passADB.setPositiveButton(R.string.save) { dialog: DialogInterface, _: Int ->
				val password = confirmPasswordInput!!.editText!!.text.toString().trim()
				if (login!!.secure == Secure.hash(password)) {
					updateUser()
				} else {
					Toast.makeText(requireContext(), getString(R.string.wrong_password), Toast.LENGTH_SHORT).show()
				}
				dialog.dismiss()
			}
			passADB.show()
		}
	}

	private val passwordDialog: AlertDialog.Builder
		get() {
			val passADB = AlertDialog.Builder(requireContext()).setTitle(R.string.old_password)
			val dialogView = layoutInflater.inflate(R.layout.password_field, LinearLayout(requireContext()), false)
			confirmPasswordInput = dialogView.findViewById(R.id.confirm_password)
			passADB.setView(dialogView)
			return passADB
		}

	private fun updateUser() {
		saveButton!!.isEnabled = false
		viewModel!!.updateUser(login!!, user!!).observe(viewLifecycleOwner, Observer { newUser: User? ->
			saveButton!!.isEnabled = true
			if (newUser == null) {
				val error = viewModel!!.error
				Log.e(TAG, "updateUser: " + getString(R.string.something_went_wrong) + error.toString())
				Toast.makeText(requireContext(),
						getString(R.string.something_went_wrong) + error.toString(),
						Toast.LENGTH_SHORT).show()
				return@Observer
			}
			login = Login(newUser.email, login!!.secure, login!!.provider)
			if (user!!.secure != null && user!!.secure!!.isNotEmpty()) {
				login!!.secure = user!!.secure
			}
			Preferences.setLogin(sp, login)
			Preferences.setLoggedUser(sp, newUser)
			Toast.makeText(requireContext(), getString(R.string.user_saved), Toast.LENGTH_SHORT)
					.show()
			Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
					.popBackStack()
		})
	}

	companion object {
		private val TAG = EditProfileFragment::class.java.simpleName
	}
}