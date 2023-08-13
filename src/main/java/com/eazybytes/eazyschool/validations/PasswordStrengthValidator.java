package com.eazybytes.eazyschool.validations;

import com.eazybytes.eazyschool.annotation.PasswordValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PasswordStrengthValidator implements ConstraintValidator<PasswordValidator,String> {

    List<String> weakPassword;
    @Override
    public void initialize(PasswordValidator constraintAnnotation) {
        weakPassword= Arrays.asList("password","12345","qwerty");
    }

    @Override
    public boolean isValid(String passwordField, ConstraintValidatorContext context) {
        return passwordField !=null && (!weakPassword.contains(passwordField));
    }
}
