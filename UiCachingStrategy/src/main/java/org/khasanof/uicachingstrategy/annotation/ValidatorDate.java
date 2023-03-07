package org.khasanof.uicachingstrategy.annotation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.khasanof.uicachingstrategy.utils.BaseUtils;

import java.time.format.DateTimeFormatter;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/1/2023
 * <br/>
 * Time: 9:44 AM
 * <br/>
 * Package: org.khasanof.uicaching.annotation
 */
public class ValidatorDate implements ConstraintValidator<ValidDate, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if (!BaseUtils.isValid(s, DateTimeFormatter.ISO_LOCAL_DATE_TIME)) {
            throw new RuntimeException("Invalid Date!");
        }
        return true;
    }
}
