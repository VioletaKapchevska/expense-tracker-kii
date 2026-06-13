package com.example.project.exceptions;

public class EmailAlreadyExistsException extends RuntimeException{
    public String message;

    public EmailAlreadyExistsException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
