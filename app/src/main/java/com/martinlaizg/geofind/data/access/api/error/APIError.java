package com.martinlaizg.geofind.data.access.api.error;

public class APIError {

	private final ErrorType type;
	private final String message;

	public APIError(ErrorType type, String message) {
		this.type = type;
		this.message = message;
	}
}
