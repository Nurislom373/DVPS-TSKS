package org.khasanof.uicachingstrategy.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/1/2023
 * <br/>
 * Time: 9:43 AM
 * <br/>
 * Package: org.khasanof.uicaching.annotation
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidatorDate.class)
@Documented
public @interface ValidDate {

    String message() default "Invalid.Date";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };

}
