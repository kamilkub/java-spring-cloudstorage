package com.cloudstorage.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<PasswordStrength, String> {
    @Override
    public void initialize(PasswordStrength constraintAnnotation) { }
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {

        return false;
    }
}
