package com.example.gridge.exception;

public class UserStatusException extends RuntimeException {
    public UserStatusException(String message) {
        super(message);
    }
}