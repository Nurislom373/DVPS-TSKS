package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MatchScope;

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

    String[] values() default {};

    MatchScope scope() default MatchScope.EQUALS;

}
