package com.example.productmaster.Exception;


public class FailedToSaveProductException extends RuntimeException {
  public FailedToSaveProductException(String message) {
    super(message);
  }
}
