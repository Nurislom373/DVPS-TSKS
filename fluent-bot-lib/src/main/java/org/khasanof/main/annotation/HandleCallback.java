package org.khasanof.main.annotation;

import org.khasanof.core.enums.MessageScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 10:53
 * <br/>
 * Package: org.khasanof.main.annotation
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleCallback {

    String value() default "";

    MessageScope scope() default MessageScope.EQUALS;

}
