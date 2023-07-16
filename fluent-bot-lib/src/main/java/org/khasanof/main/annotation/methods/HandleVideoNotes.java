package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MultiMatchScope;
import org.khasanof.main.annotation.extra.TGPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 09.07.2023 16:53
 */
@TGPermission
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleVideoNotes {

    HandleVideoNote[] values();

    MultiMatchScope match() default MultiMatchScope.ANY_MATCH;
}
