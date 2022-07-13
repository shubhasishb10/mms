package com.mms.thp.validation.annotation.impl;

import com.mms.thp.validation.annotation.CheckValue;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class FieldValueChecker implements ConstraintValidator<CheckValue, Integer> {

    private int expectedMinimumValue;

    @Override
    public void initialize(CheckValue annotation) {
        expectedMinimumValue = annotation.expectedCount();
    }
    @Override
    public boolean isValid(Integer givenValue, ConstraintValidatorContext constraintValidatorContext) {
        return givenValue > expectedMinimumValue;
    }
}
