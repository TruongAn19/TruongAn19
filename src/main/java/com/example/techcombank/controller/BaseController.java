package com.example.techcombank.controller;

import com.example.techcombank.exception.ApiException;
import com.example.techcombank.exception.ErrorCode;
import com.example.techcombank.exception.UserValidatorException;
import com.example.techcombank.models.ResponseObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class BaseController {

    protected ResponseEntity<ResponseObject> handleValidationErrors(BindingResult result) {
        StringBuilder errorMessages = new StringBuilder();
        String password = (String) result.getFieldValue("password");

        if (validateField(result, "lastName", ErrorCode.VALIDATION_EMPTY.getMessage("LastName"), errorMessages) ||
                validateField(result, "firstName", ErrorCode.VALIDATION_EMPTY.getMessage("FirstName"), errorMessages) ||
                validateField(result, "userName", ErrorCode.VALIDATION_EMPTY.getMessage("UserName"), errorMessages) ||
                validateField(result, "password", ErrorCode.VALIDATION_EMPTY.getMessage("Password"), errorMessages)) {
            throw new UserValidatorException(errorMessages.toString());
        }

        if (password != null) {
            boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+{}|:<>?~.".indexOf(ch) >= 0);
            boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);

            if (password.length() < 8) {
                errorMessages.append(ErrorCode.WARNING_LENGTH_PASSWORD.getMessage());
            }

            if (!hasSpecial && !hasUppercase) {
                errorMessages.append(ErrorCode.WARNING_PASSWORD.getMessage());
            }

            if (errorMessages.length() > 0) {
                throw new UserValidatorException(errorMessages.toString());
            }
        }

        return null;
    }

    private boolean validateField(BindingResult result, String fieldName, String message, StringBuilder errorMessages) {
        if (result.hasFieldErrors(fieldName)) {
            result.getFieldErrors(fieldName).forEach(error -> {
                errorMessages.append(message);
            });
            return true;
        }
        return false;
    }

    @ExceptionHandler(UserValidatorException.class)
    public ResponseEntity<ResponseObject> handleUserValidationException(UserValidatorException ex) {
        return new ResponseEntity<>(new ResponseObject("Error", ex.getMessage(), null), HttpStatus.BAD_REQUEST);
    }
}
