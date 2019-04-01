package com.martinlaizg.geofind.data.access.retrofit.error;

public class APIError {

	private ErrorType type;
	private String message;


	public ErrorType getType() {
		return type;
	}

	public void setType(ErrorType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
