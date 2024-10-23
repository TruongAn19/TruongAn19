package com.example.techcombank.exception;

import com.example.techcombank.models.ResponseObject;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalException {

    @ExceptionHandler(value = ApiException.class)
    ResponseEntity<ResponseObject> handlingApiException(ApiException exception) {
        ErrorCode errorCode = exception.getErrCode();
        ResponseObject responseObject = new ResponseObject();

        responseObject.setStatus(String.valueOf(errorCode.getCode()));
        responseObject.setMessage(errorCode.getMessage());


        return ResponseEntity.badRequest().body(responseObject);
    }

    @ExceptionHandler(value =  MethodArgumentNotValidException.class)
    ResponseEntity<ResponseObject> handlingValidation(MethodArgumentNotValidException exception) {
        String enumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.INVALID_KEY;
        try{
            errorCode = ErrorCode.valueOf(enumKey);
            var constraintViolation = exception.getBindingResult()
                    .getAllErrors().getFirst().unwrap(ConstraintViolation.class);
            var attributes = constraintViolation.getConstraintDescriptor().getAttributes();

        } catch (IllegalArgumentException e){}

        ResponseObject responseObject = new ResponseObject();
        responseObject.setStatus(errorCode.getCode());
        responseObject.setMessage(errorCode.getMessage());
        return ResponseEntity.badRequest().body(responseObject);
    }
}
