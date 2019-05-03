package com.martinlaizg.geofind.data.access.api.error;

import com.google.gson.annotations.SerializedName;
import com.martinlaizg.geofind.R;

public enum ErrorType {
	@SerializedName("other") OTHER(R.string.other_error),
	@SerializedName("parse") PARSE(R.string.parse_error),
	@SerializedName("other") NETWORK(R.string.network_error),
	@SerializedName("email") EMAIL(R.string.email_error),
	@SerializedName("id") ID(R.string.id_error),
	@SerializedName("completed") COMPLETED(R.string.tour_is_completed);

	private int message;

	ErrorType(int message) {
		this.message = message;
	}

	public int getMessage() {
		return message;
	}
}
