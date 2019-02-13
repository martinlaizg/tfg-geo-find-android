package com.martinlaizg.geofind.client.error;

public class APIError {

    private int error;
    private String message;

    public APIError() {
    }

    public APIError(final int error, final String message) {
        this.error = error;
        this.message = message;
    }

    public String getMessage() {
        return this.message;
    }

}
