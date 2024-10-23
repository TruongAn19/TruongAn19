package com.example.techcombank.exception;

public enum SuccessResponsive {
    RESPONSE_OK("Oke");

    SuccessResponsive(String message) {
        this.message = message;
    }

    private String message;


    public String getMessage() {return message;}
}
