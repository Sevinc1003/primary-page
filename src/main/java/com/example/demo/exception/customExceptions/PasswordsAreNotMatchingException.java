package com.example.demo.exception.customExceptions;

public class PasswordsAreNotMatchingException extends RuntimeException{

    public PasswordsAreNotMatchingException(String message) {
        super(message);
    }

    

}
