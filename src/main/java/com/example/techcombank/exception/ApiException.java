package com.example.techcombank.exception;

public class ApiException extends RuntimeException {

    private ErrorCode errCode;

    public ApiException(ErrorCode errCode) {
        super(errCode.getMessage());
        this.errCode = errCode;
    }

    public ErrorCode getErrCode() {
        return errCode;
    }

    public void setErrCode(ErrorCode errCode) {
        this.errCode = errCode;
    }
}