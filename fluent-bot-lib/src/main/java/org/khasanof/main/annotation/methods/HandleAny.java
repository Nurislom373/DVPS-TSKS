package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.HandleType;
import org.khasanof.core.enums.Proceed;
import org.khasanof.main.annotation.extra.TGPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see org.khasanof.main.annotation
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @since 20.06.2023
 */
@TGPermission
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleAny {

    HandleType type();

    Proceed proceed() default Proceed.PROCEED;

}
