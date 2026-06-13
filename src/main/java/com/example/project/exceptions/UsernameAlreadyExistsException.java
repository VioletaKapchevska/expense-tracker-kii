package com.example.project.exceptions;

public class UsernameAlreadyExistsException extends RuntimeException{
    public String message;

    public UsernameAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
