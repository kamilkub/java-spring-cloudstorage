package com.cloudstorage.annotation;


import javax.validation.Constraint;
import java.lang.annotation.*;

@Documented
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Constraint(validatedBy = PasswordValidator.class)
public @interface PasswordStrength {
    String password() default "Password strength is to weak";

}
