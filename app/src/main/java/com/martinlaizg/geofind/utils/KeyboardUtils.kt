package com.martinlaizg.geofind.utils

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    /**
     * Hide the keyboard
     *
     * @param activity
     * the activity
     */
    fun hideKeyboard(activity: Activity) {
        val editTextInput = activity
                .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        val currentFocus = activity.currentFocus
        if (currentFocus != null && editTextInput != null) {
            editTextInput.hideSoftInputFromWindow(currentFocus.windowToken, 0)
        }
    }
}