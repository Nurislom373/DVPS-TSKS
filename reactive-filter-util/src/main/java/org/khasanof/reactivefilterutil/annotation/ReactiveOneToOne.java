package org.khasanof.reactivefilterutil.annotation;

import java.lang.annotation.*;

/**
 * @author Nurislom
 * @see org.khasanof.reactivefilterutil.annotation
 * @since 02.08.2023 8:36
 */
@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReactiveOneToOne {

    Class<?> value();

}
