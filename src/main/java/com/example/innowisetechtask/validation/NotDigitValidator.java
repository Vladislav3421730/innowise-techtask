package com.example.innowisetechtask.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotDigitValidator implements ConstraintValidator<NotDigit, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return !str.matches(".*\\d.*");
    }
}
