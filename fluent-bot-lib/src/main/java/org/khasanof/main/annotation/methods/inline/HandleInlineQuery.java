package org.khasanof.main.annotation.methods.inline;

import org.khasanof.core.enums.MatchScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods.inline
 * @since 29.07.2023 0:43
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleInlineQuery {

    String[] value() default {};

    MatchScope match() default MatchScope.EQUALS;

}
