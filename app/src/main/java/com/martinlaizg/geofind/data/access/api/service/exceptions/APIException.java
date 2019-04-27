package com.martinlaizg.geofind.data.access.api.service.exceptions;

import com.martinlaizg.geofind.data.access.api.error.APIError;
import com.martinlaizg.geofind.data.access.api.error.ErrorType;

public class APIException
		extends Throwable {

	private ErrorType type;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public APIException(ErrorType type, String message) {
		this.type = type;
		this.message = message;
	}

	public ErrorType getType() {
		return type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}

	public APIError getAPIError() {
		return new APIError(type, message);
	}
}
