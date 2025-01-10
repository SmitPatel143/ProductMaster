package com.example.productmaster.Exception;

public class UserIsAlreadyRegisteredException extends RuntimeException {
    public UserIsAlreadyRegisteredException(String message) {
        super(message);
    }
}
