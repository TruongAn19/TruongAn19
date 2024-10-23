//package com.example.techcombank.validator;
//
//import com.example.techcombank.validator.users_validator.UserValidator;
//import jakarta.validation.Constraint;
//import jakarta.validation.Payload;
//
//import java.lang.annotation.ElementType;
//import java.lang.annotation.Retention;
//import java.lang.annotation.RetentionPolicy;
//import java.lang.annotation.Target;
//
//@Constraint(validatedBy = {UserValidator.class})
//@Target({ ElementType.FIELD, ElementType.PARAMETER })
//@Retention(RetentionPolicy.RUNTIME)
//public @interface ValidUser {
//    String message() default "Invalid user"; // Thông điệp mặc định
//    Class<?>[] groups() default {}; // Các nhóm
//    Class<? extends Payload>[] payload() default {}; // Thông tin bổ sung
//}
