package com.example.productmaster.Exception;

public class ConfirmationTokenExpiredException extends RuntimeException {
    public ConfirmationTokenExpiredException(String message) {
        super(message);
    }
}
