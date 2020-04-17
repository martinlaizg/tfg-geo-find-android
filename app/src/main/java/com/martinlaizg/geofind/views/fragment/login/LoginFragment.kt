package com.martinlaizg.geofind.views.fragment.login

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.util.Patterns
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.Navigation
import androidx.preference.PreferenceManager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.Secure
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.utils.KeyboardUtils
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel
import java.util.*

class LoginFragment : Fragment(), View.OnClickListener {
	@kotlin.jvm.JvmField
	@BindView(R.id.email_input)
	var email_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.password_input)
	var password_input: TextInputLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.login_button)
	var login_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.login_register_button)
	var registry_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.fake_google_sign_in_button)
	var fake_google_sign_in_button: MaterialButton? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.load_layout)
	var load_layout: ConstraintLayout? = null

	@kotlin.jvm.JvmField
	@BindView(R.id.login_scroll)
	var login_scroll: ScrollView? = null
	private var viewModel: LoginViewModel? = null
	private var mGoogleSignInClient: GoogleSignInClient? = null
	private var sub: String? = null
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		if (requestCode == RC_SIGN_IN) {
			login_scroll!!.setBackgroundColor(
					resources.getColor(android.R.color.background_light, null))
			val task = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleSignInResult(task)
			return
		}
		super.onActivityResult(requestCode, resultCode, data)
	}

	private fun handleSignInResult(task: Task<GoogleSignInAccount>) {
		try {
			val account = task.getResult(ApiException::class.java)
			if (account != null) {
				val email = account.email
				val idToken = account.idToken
				sub = account.id
				val l = Login(email, idToken, Login.Provider.GOOGLE)
				login(l)
			} else {
				Toast.makeText(requireContext(), getString(R.string.wrong_login),
						Toast.LENGTH_SHORT).show()
			}
		} catch (e: ApiException) {
			Log.w(TAG, "signInResult:failed code=" + e.statusCode)
		}
	}

	private fun login(login: Login) {
		viewModel!!.login(login).observe(this, Observer { user: User? ->
			email_input!!.error = ""
			password_input!!.error = ""
			if (user == null) {
				val e = viewModel.getError()
				when (e) {
					ErrorType.EMAIL -> email_input!!.error = getString(R.string.wrong_email)
					ErrorType.PASSWORD -> password_input!!.error = getString(R.string.wrong_password)
					else -> e!!.showToast(requireContext())
				}
			} else {
				val sp = PreferenceManager
						.getDefaultSharedPreferences(requireContext())
				Preferences.setLoggedUser(sp, user)
				if (login.provider != Login.Provider.OWN) login.secure = sub
				Preferences.setLogin(sp, login)
				Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
						.navigate(R.id.toMain)
			}
			login_button!!.isEnabled = true
			registry_button!!.isEnabled = true
			load_layout!!.visibility = View.GONE
		})
		KeyboardUtils.hideKeyboard(requireActivity())
	}

	override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
	                          savedInstanceState: Bundle?): View? {
		val view = inflater.inflate(R.layout.fragment_login, container, false)
		ButterKnife.bind(this, view)
		fake_google_sign_in_button!!.setOnClickListener { v: View? -> googleSignIn() }
		// Google SignIn Button
		val gso = GoogleSignInOptions.Builder(
				GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.client_id))
				.requestEmail().build()
		mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
		return view
	}

	private fun googleSignIn() {
		val signInIntent = mGoogleSignInClient!!.signInIntent
		startActivityForResult(signInIntent, RC_SIGN_IN)
	}

	override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
		super.onViewCreated(view, savedInstanceState)
		viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
		Objects.requireNonNull(password_input!!.editText)
				.setOnEditorActionListener { v: TextView?, actionId: Int, event: KeyEvent? ->
					if (actionId == EditorInfo.IME_ACTION_DONE) {
						login_button!!.performClick()
						return@setOnEditorActionListener true
					}
					false
				}
		login_button!!.setOnClickListener(this)
		registry_button
				.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.toRegistry))
	}

	override fun onStart() {
		val account = GoogleSignIn.getLastSignedInAccount(requireContext())
		if (account != null) {
			val email = account.email
			val idToken = account.idToken
			val l = Login(email, idToken, Login.Provider.GOOGLE)
			login(l)
		}
		super.onStart()
	}

	override fun onClick(v: View) {
		val email = Objects.requireNonNull(email_input!!.editText).text.toString()
				.trim { it <= ' ' }
		val password = Objects.requireNonNull(password_input!!.editText).text.toString()
				.trim { it <= ' ' }
		if (TextUtils.isEmpty(email)) {
			email_input!!.error = getString(R.string.required_email)
			return
		}
		if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
			email_input!!.error = getString(R.string.email_wrong_format)
			return
		}
		if (TextUtils.isEmpty(Objects.requireNonNull(password_input!!.editText).text)) {
			password_input!!.error = getString(R.string.required_password)
			return
		}
		login_button!!.isEnabled = false
		registry_button!!.isEnabled = false
		load_layout!!.visibility = View.VISIBLE
		val l = Login(email, Secure.hash(password))
		login(l)
	}

	companion object {
		private val TAG = LoginFragment::class.java.simpleName
		private const val RC_SIGN_IN = 0
	}
}