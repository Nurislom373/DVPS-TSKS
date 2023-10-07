package org.khasanof.springbootstarterfluent.main.annotation.methods;

import org.khasanof.springbootstarterfluent.core.enums.HandleType;
import org.khasanof.springbootstarterfluent.core.enums.Proceed;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see org.khasanof.main.annotation
 * @author <a href="https://github.com/Nurislom373">Nurislom</a>
 * @since 20.06.2023
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleAny {

    HandleType type() default HandleType.MESSAGE;

    Proceed proceed() default Proceed.PROCEED;

}
