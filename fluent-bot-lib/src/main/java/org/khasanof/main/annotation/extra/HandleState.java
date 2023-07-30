package org.khasanof.main.annotation.extra;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.extra
 * @since 09.07.2023 18:50
 */
@java.lang.annotation.Target({java.lang.annotation.ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
public @interface HandleState {

    String value();

    boolean proceedHandleMethods() default false;

}
