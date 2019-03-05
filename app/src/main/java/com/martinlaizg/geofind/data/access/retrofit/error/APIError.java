package com.martinlaizg.geofind.data.access.retrofit.error;

import androidx.annotation.NonNull;

public class APIError {

    private int statusCode;
    private String message;

    public APIError() {
    }

    public int status() {
        return statusCode;
    }

    public String message() {
        return message;
    }

    @NonNull
    @Override
    public String toString() {
        return "Status code: " + statusCode + ", message: " + message;
    }
}
