package com.example.innowisetechtask.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = PasswordValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Password {
    String message() default "Password must be at least 8 characters long, contain at least one digit," +
            " one uppercase letter, one lowercase letter, one special character, and have no whitespace.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
