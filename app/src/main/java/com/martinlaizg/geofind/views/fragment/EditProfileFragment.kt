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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.Secure
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.views.viewmodel.EditProfileViewModel
import com.squareup.picasso.Picasso
import java.util.*

class EditProfileFragment : Fragment() {
	@kotlin.jvm.JvmField
	@BindView(R.id.user_image)
	var user_image: ImageView? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.name_input)
	var name_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.username_input)
	var username_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.email_input)
	var email_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.change_image_button)
	var change_image_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.save_button)
	var save_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.new_password_input)
	var new_password_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.c_new_password_input)
	var c_new_password_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.change_password_button)
	var change_password_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.password_layout)
	var password_layout: ConstraintLayout? = null
	private var confirm_password_input: TextInputLayout? = null
	private var sp: SharedPreferences? = null
	private var user: User? = null
	private var login: Login? = null
	private var viewModel: EditProfileViewModel? = null
	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
		ButterKnife.bind(this, view)
		return view
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
		fillUserData()
		login = Preferences.getLogin(sp)
		if (login.getProvider() != Login.Provider.OWN) {
			password_layout!!.visibility = View.GONE
			email_input!!.visibility = View.GONE
		}
		viewModel = ViewModelProviders.of(this).get(EditProfileViewModel::class.java)

		// Buttons
		change_image_button!!.setOnClickListener { v: View? ->
			Toast
					.makeText(requireContext(), getString(R.string.work_in_progress),
							Toast.LENGTH_SHORT).show()
		}
		change_image_button!!.visibility = View.GONE
		save_button!!.setOnClickListener { v: View? -> saveAction() }
		change_password_button!!.setOnClickListener { v: View? -> changePasswordAction() }
	}

	private fun fillUserData() {
		user = Preferences.getLoggedUser(sp)
		login = Preferences.getLogin(sp)
		if (user.getName() != null) {
			Objects.requireNonNull(name_input!!.editText).setText(user.getName())
		}
		if (user.getUsername() != null) {
			Objects.requireNonNull(username_input!!.editText).setText(user.getUsername())
		}
		if (user.getEmail() != null) {
			Objects.requireNonNull(email_input!!.editText).setText(user.getEmail())
		}
		if (user.getImage() != null && !user.getImage().isEmpty()) {
			Picasso.with(requireContext()).load(user.getImage()).into(user_image)
		}
	}

	private fun saveAction() {
		val name = Objects.requireNonNull(name_input!!.editText).text.toString().trim { it <= ' ' }
		val username = Objects.requireNonNull(username_input!!.editText).text.toString()
				.trim { it <= ' ' }
		val email = Objects.requireNonNull(email_input!!.editText).text.toString()
				.trim { it <= ' ' }
		user = Preferences.getLoggedUser(sp)
		user.setName(name)
		user.setUsername(username)
		login = Preferences.getLogin(sp)
		if (login.getProvider() == Login.Provider.OWN) {
			user.setEmail(email)
			val passADB = passwordDialog
			passADB.setPositiveButton(R.string.save) { dialog: DialogInterface, which: Int ->
				val password = Objects.requireNonNull(confirm_password_input!!.editText)
						.text.toString().trim { it <= ' ' }
				if (login.getSecure() == Secure.hash(password)) {
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
		val newPassword = Objects.requireNonNull(new_password_input!!.editText).text
				.toString().trim { it <= ' ' }
		val cnewPassword = Objects.requireNonNull(c_new_password_input!!.editText).text
				.toString().trim { it <= ' ' }
		if (newPassword.isEmpty()) {
			new_password_input!!.error = getString(R.string.required_password)
			return
		}
		if (newPassword != cnewPassword) {
			c_new_password_input!!.error = getString(R.string.password_does_not_match)
			return
		}
		user = Preferences.getLoggedUser(sp)
		user.setSecure(Secure.hash(newPassword))
		login = Preferences.getLogin(sp)
		if (login.getProvider() == Login.Provider.OWN) {
			val passADB = passwordDialog
			passADB.setPositiveButton(R.string.save) { dialog: DialogInterface, which: Int ->
				val password = Objects.requireNonNull(confirm_password_input!!.editText)
						.text.toString().trim { it <= ' ' }
				if (login.getSecure() == Secure.hash(password)) {
					updateUser()
				} else {
					Toast.makeText(requireContext(), getString(R.string.wrong_password),
							Toast.LENGTH_SHORT).show()
				}
				dialog.dismiss()
			}
			passADB.show()
		}
	}

	private val passwordDialog: AlertDialog.Builder
		private get() {
			val passADB = AlertDialog.Builder(requireContext())
					.setTitle(R.string.old_password)
			val dialogView = layoutInflater
					.inflate(R.layout.password_field, LinearLayout(requireContext()), false)
			confirm_password_input = dialogView.findViewById(R.id.confirm_password)
			passADB.setView(dialogView)
			return passADB
		}

	private fun updateUser() {
		save_button!!.isEnabled = false
		viewModel!!.updateUser(login, user).observe(this, Observer { newUser: User? ->
			save_button!!.isEnabled = true
			if (newUser == null) {
				val error = viewModel.getError()
				Log.e(TAG,
						"updateUser: " + getString(R.string.something_went_wrong) + error.toString())
				Toast.makeText(requireContext(),
						getString(R.string.something_went_wrong) + error.toString(),
						Toast.LENGTH_SHORT).show()
				return@observe
			}
			login = Login(newUser.email, login.getSecure(), login.getProvider())
			if (user.getSecure() != null && !user.getSecure().isEmpty()) {
				login.setSecure(user.getSecure())
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