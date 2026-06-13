package com.example.project.exceptions;

public class TransactionNotValidException extends RuntimeException{
    public String message;

    public TransactionNotValidException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
