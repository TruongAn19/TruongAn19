package com.example.techcombank.exception;


public class UserValidatorException extends RuntimeException{
    // Constructor không tham số
    public UserValidatorException() {
        super();
    }

    // Constructor nhận tham số là message
    public UserValidatorException(String message) {
        super(message);
    }
}
