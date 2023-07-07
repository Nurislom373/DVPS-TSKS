package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MultiMatchScope;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 06.07.2023 22:59
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleVideos {

    HandleVideo[] values();

    MultiMatchScope match() default MultiMatchScope.ANY_MATCH;

}
