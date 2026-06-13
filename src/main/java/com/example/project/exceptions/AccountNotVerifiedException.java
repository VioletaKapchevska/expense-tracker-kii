package com.example.project.exceptions;

public class AccountNotVerifiedException extends RuntimeException{
        public AccountNotVerifiedException(){
            super("Your account is not verified!");
        }
}
