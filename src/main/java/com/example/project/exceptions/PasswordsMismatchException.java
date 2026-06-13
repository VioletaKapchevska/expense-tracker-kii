package com.example.project.exceptions;

public class PasswordsMismatchException extends RuntimeException{
    public String message;

    public PasswordsMismatchException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
