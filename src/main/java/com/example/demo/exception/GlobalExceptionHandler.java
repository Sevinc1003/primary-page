package com.example.demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.demo.exception.customExceptions.PasswordsAreNotMatchingException;
import com.example.demo.exception.customExceptions.UserAlreadyExistsException;

@RestControllerAdvice
public class GlobalExceptionHandler {

 @ExceptionHandler(UserAlreadyExistsException.class)
public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.CONFLICT.value(),
            "User already exists",
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    
}


@ExceptionHandler(PasswordsAreNotMatchingException.class)
public ResponseEntity<ErrorResponse> handlePasswordsAreNotMatchingException(
        PasswordsAreNotMatchingException ex) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Passwords do not match",
            ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ErrorResponse> handleValidationException(
        MethodArgumentNotValidException ex) {

    ErrorResponse error = new ErrorResponse(
            HttpStatus.BAD_REQUEST.value(),
            "Validation failed",
             ex.getBindingResult()
                    .getFieldErrors()
                    .get(0)
                    .getDefaultMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
}
    
}