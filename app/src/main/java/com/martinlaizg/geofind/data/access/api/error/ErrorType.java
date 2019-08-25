package com.martinlaizg.geofind.data.access.api.error;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.annotations.SerializedName;
import com.martinlaizg.geofind.R;

/**
 * Types of error from de server
 */
public enum ErrorType {
	@SerializedName("other") OTHER(R.string.other_error),
	@SerializedName("other") NETWORK(R.string.network_error),
	@SerializedName("username") USERNAME(R.string.username_error),
	@SerializedName("email") EMAIL(R.string.email_error),
	@SerializedName("password") PASSWORD(R.string.password_error),
	@SerializedName("id") ID(R.string.id_error),
	@SerializedName("tour_id") TOUR_ID(R.string.tour_id_error),
	@SerializedName("user_id") USER_ID(R.string.user_id_error),
	@SerializedName("exist") EXIST(R.string.existence_error),
	@SerializedName("token") TOKEN(R.string.token_error),
	@SerializedName("provider") PROVIDER(R.string.provider_error),
	@SerializedName("provider_login") PROVIDER_LOGIN(R.string.provider_login_error),
	@SerializedName("secure") SECURE(R.string.secure_error),
	@SerializedName("auth") AUTH(R.string.auth_error),
	@SerializedName("expired") EXPIRED(R.string.expired_error),
	COMPLETE(R.string.completed),
	;

	private static final String TAG = ErrorType.class.getSimpleName();
	private final int message;

	ErrorType(int message) {
		this.message = message;
	}

	/**
	 * Show the appropriate toast
	 *
	 * @param context
	 * 		the context
	 */
	public void showToast(Context context) {
		Toast.makeText(context, context.getString(message), Toast.LENGTH_SHORT).show();
		Log.e(TAG, context.getString(message));
	}
}
