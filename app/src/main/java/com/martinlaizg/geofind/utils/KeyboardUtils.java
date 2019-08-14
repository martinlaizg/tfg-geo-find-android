package com.martinlaizg.geofind.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class KeyboardUtils {

	/**
	 * Hide the keyboard
	 *
	 * @param activity
	 * 		the activity
	 */
	public static void hideKeyboard(Activity activity) {
		InputMethodManager editTextInput = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		View currentFocus = activity.getCurrentFocus();
		if(currentFocus != null && editTextInput != null) {
			editTextInput.hideSoftInputFromWindow(currentFocus.getWindowToken(), 0);
		}
	}
}
