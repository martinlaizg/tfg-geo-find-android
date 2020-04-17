package com.martinlaizg.geofind.data.access.api.error

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.martinlaizg.geofind.R

/**
 * Types of error from de server
 */
enum class ErrorType(private val message: Int) {
    OTHER(R.string.other_error), NETWORK(R.string.network_error), USERNAME(R.string.username_error), EMAIL(R.string.email_error), PASSWORD(R.string.password_error), ID(R.string.id_error), TOUR_ID(R.string.tour_id_error), USER_ID(R.string.user_id_error), EXIST(R.string.existence_error), TOKEN(R.string.token_error), PROVIDER(R.string.provider_error), PROVIDER_LOGIN(R.string.provider_login_error), SECURE(R.string.secure_error), AUTH(R.string.auth_error), EXPIRED(R.string.expired_error), COMPLETE(R.string.completed);

    /**
     * Show the appropriate toast
     *
     * @param context
     * the context
     */
    fun showToast(context: Context) {
        Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show()
        Log.e(TAG, context.getString(message))
    }

    companion object {
        private val TAG = ErrorType::class.java.simpleName
    }

}