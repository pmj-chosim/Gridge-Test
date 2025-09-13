package com.example.gridge.exception;

public class ConsentExpiredException extends RuntimeException {
    public ConsentExpiredException(String message) {
        super(message);
    }
}