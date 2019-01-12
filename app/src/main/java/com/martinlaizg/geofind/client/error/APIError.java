package com.martinlaizg.geofind.client.error;

public class APIError {

    private int error;
    private String message;

    public APIError() {
    }

    public APIError(int error, String message) {
        this.error = error;
        this.message = message;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
