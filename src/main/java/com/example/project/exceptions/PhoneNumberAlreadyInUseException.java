package com.example.project.exceptions;

public class PhoneNumberAlreadyInUseException extends RuntimeException{
    public String message;

    public PhoneNumberAlreadyInUseException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
