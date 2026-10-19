package com.example.demo.exception.customExceptions;

public class EmailAlreadyTakenException extends RuntimeException {

    public EmailAlreadyTakenException() {
        super("This email is already taken.");
    }



}
