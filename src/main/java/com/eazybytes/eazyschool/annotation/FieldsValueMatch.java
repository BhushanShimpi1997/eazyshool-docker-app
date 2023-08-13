package com.eazybytes.eazyschool.annotation;

import com.eazybytes.eazyschool.validations.FieldsValueMatchValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = FieldsValueMatchValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsValueMatch {
    String message() default "Fields Value DO Not Match !!";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String field();
    String fieldMatch();

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @interface List{
        FieldsValueMatch [] value();
    }
}
