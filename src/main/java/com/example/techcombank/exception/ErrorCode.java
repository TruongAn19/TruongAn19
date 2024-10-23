package com.example.techcombank.exception;

public enum ErrorCode {
    INVALID_KEY("", ""),
    USER_EXISTED ("1", "User already existed"),
    USER_CANNOT_FIND("106", "Can not found user by id"),
    CANNOT_FIND_USERNAME("107", "Can not find userName"),
    INCORRECT_PASSWORD("108", "Mật khẩu không đúng"),
    WARNING_PASSWORD("", "Mật khẩu phải có ít nhất một ký tự viết hoa hoặc ký tự đặc biệt;"),
    WARNING_LENGTH_PASSWORD("", "Mật khẩu phải có ít nhất 8 ký tự; "),
    VALIDATION_EMPTY("", "Không được để trống %s")
    ;

    ErrorCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    private String code;
    private String message;

    public String getCode() {
        return code;
    }

    public String getMessage() { return message; }

    public String getMessage(String fieldName) {
        return String.format(message, fieldName);
    }

}
