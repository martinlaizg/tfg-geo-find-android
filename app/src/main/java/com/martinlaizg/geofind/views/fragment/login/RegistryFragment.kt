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
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import com.martinlaizg.geofind.R
import com.martinlaizg.geofind.config.Preferences
import com.martinlaizg.geofind.data.Secure
import com.martinlaizg.geofind.data.access.api.entities.Login
import com.martinlaizg.geofind.data.access.api.error.ErrorType
import com.martinlaizg.geofind.data.access.database.entities.User
import com.martinlaizg.geofind.views.viewmodel.LoginViewModel
import java.util.*

class RegistryFragment : Fragment() {
    @kotlin.jvm.JvmField
    @BindView(R.id.email_input)
    var email_input: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.password_input)
    var password_input: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.c_password_input)
    var c_password_input: TextInputLayout? = null

    @kotlin.jvm.JvmField
    @BindView(R.id.btn_registry)
    var btn_registry: MaterialButton? = null
    private var viewModel: LoginViewModel? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_registry, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProviders.of(requireActivity()).get(LoginViewModel::class.java)
        btn_registry!!.setOnClickListener { v: View? -> registry() }
    }

    private fun registry() {
        val email = Objects.requireNonNull(email_input!!.editText).text.toString()
                .trim { it <= ' ' }
        val password = Objects.requireNonNull(password_input!!.editText).text
                .toString().trim { it <= ' ' }
        val cPassword = Objects.requireNonNull(c_password_input!!.editText).text
                .toString().trim { it <= ' ' }
        btn_registry!!.isEnabled = false
        if (Objects.requireNonNull(email_input!!.editText).text.toString().trim { it <= ' ' }
                        .isEmpty()) {
            email_input!!.error = getString(R.string.required_password)
            return
        }
        if (password.isEmpty()) {
            password_input!!.error = getString(R.string.required_verify_password)
            return
        }
        if (cPassword.isEmpty()) {
            c_password_input!!.error = getString(R.string.required_verify_password)
            return
        }
        if (cPassword != password) {
            c_password_input!!.error = getString(R.string.password_does_not_match)
            return
        }
        val l = Login(email, Secure.hash(password))
        viewModel!!.registry(l).observe(this, Observer { user: User? ->
            btn_registry!!.isEnabled = true
            if (user == null) {
                if (viewModel.getError() == ErrorType.EMAIL) {
                    email_input!!.error = getString(R.string.email_in_use)
                } else {
                    Toast.makeText(requireContext(), getString(R.string.other_error),
                            Toast.LENGTH_SHORT).show()
                }
                return@observe
            }
            val sp = PreferenceManager.getDefaultSharedPreferences(requireContext())
            Preferences.setLoggedUser(sp, user)
            Preferences.setLogin(sp, l)
            Toast.makeText(requireActivity(), getString(R.string.registered), Toast.LENGTH_LONG)
                    .show()
            Navigation.findNavController(requireActivity(), R.id.main_fragment_holder)
                    .popBackStack()
        })
    }

    companion object {
        private val TAG = RegistryFragment::class.java.simpleName
    }
}