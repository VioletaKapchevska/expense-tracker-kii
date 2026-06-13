package com.example.project.exceptions;

public class InvalidArgumentsException extends RuntimeException{
    public InvalidArgumentsException(){
        super("Invalid credentials.");
    }
}
