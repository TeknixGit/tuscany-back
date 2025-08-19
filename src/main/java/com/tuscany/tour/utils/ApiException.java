package com.tuscany.tour.utils;

public class ApiException extends RuntimeException {
    private final String error;

    public ApiException(String message, String error) {
        super(message);
        this.error = error;
    }

    public String getError() {
        return error;
    }
}
