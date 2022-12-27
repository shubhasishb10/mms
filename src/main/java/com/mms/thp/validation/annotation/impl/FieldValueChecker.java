/**
 * Copyright (C) DreamTech Inc - All Rights Reserved Unauthorized copying of this file,
 * via any medium is strictly prohibited Proprietary and confidential For any use
 * of this software please write us to shubhasishb10@gmail.com.
 */

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
