package com.mms.thp.validation.annotation;

import com.mms.thp.validation.annotation.impl.FieldValueChecker;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = FieldValueChecker.class)
public @interface CheckValue {

    String message();
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    int expectedCount();
}
