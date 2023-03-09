package org.khasanof.uicachingstrategy.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Nurislom
 * <br/>
 * Date: 3/9/2023
 * <br/>
 * Time: 6:54 PM
 * <br/>
 * Package: org.khasanof.uicachingstrategy.annotation
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface TransactionType {
    String cardNumber() default "";

    String name() default "";
}
