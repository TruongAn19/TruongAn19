//package com.example.techcombank.validator.users_validator;
//
//import com.example.techcombank.exception.ApiException;
//import com.example.techcombank.exception.ErrorCode;
//import com.example.techcombank.exception.UserValidatorException;
//import com.example.techcombank.models.User;
//import com.example.techcombank.validator.ValidUser;
//import jakarta.validation.ConstraintValidator;
//import jakarta.validation.ConstraintValidatorContext;
//import lombok.extern.slf4j.Slf4j;
//import org.mapstruct.control.MappingControl;
//import org.springframework.util.ObjectUtils;
//@Slf4j
//public class UserValidator implements ConstraintValidator<ValidUser, User> {
//
//
//    @Override
//    public void initialize(ValidUser constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
//    }
//
//    @Override
//    public boolean isValid(User value, ConstraintValidatorContext context) {
//        boolean isErrored = false;
//        String password = value.getPassword();
//        StringBuilder errorMessages = new StringBuilder();
//
//        // Kiểm tra ký tự đặc biệt
//        boolean hasSpecial = password.chars().anyMatch(ch -> "!@#$%^&*()_+{}|:<>?~.".indexOf(ch) >= 0);
//
//        // Kiểm tra ký tự viết hoa
//        boolean hasUppercase = password.chars().anyMatch(Character::isUpperCase);
//
//        // Kiểm tra họ và tên
//        if (ObjectUtils.isEmpty(value.getLastName())) {
//            isErrored = true;
//        }
//
//        if (ObjectUtils.isEmpty(value.getFirstName())) {
//            isErrored = true;
//        }
//
//        // Kiểm tra mật khẩu có bị rỗng không
//        if (ObjectUtils.isEmpty(password)) {
//            isErrored = true;
//        }
//
//        // Kiểm tra tên người dùng có bị rỗng không
//        if (ObjectUtils.isEmpty(value.getUserName())) {
//            context.disableDefaultConstraintViolation();
//            context.buildConstraintViolationWithTemplate("Tên đăng nhập không được để trống").addConstraintViolation();
//            isErrored = true;
//        }
//
//        return !isErrored;  // Trả về false nếu có lỗi
//    }
//}
