package com.example.productmaster.Exception;

public class AccountIsNotVerifiedException extends RuntimeException {
    public AccountIsNotVerifiedException(String message) {
        super(message);
    }
}
