package com.martinlaizg.geofind.data.access.api.service.exceptions;

import com.martinlaizg.geofind.data.access.api.error.ErrorType;

/**
 * Exception from API
 */
public class APIException
		extends Throwable {

	private final ErrorType type;
	private final String message;

	public String getMessage() {
		return message;
	}

	public APIException(ErrorType type) {
		this.type = type;
		this.message = null;
	}

	public APIException(ErrorType type, String message) {
		this.type = type;
		this.message = message;
	}

	public ErrorType getType() {
		return type;
	}

}
