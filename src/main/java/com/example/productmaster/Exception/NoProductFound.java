package com.example.productmaster.Exception;

public class NoProductFound extends RuntimeException {
    public NoProductFound(String message) {
        super(message);
    }
}
