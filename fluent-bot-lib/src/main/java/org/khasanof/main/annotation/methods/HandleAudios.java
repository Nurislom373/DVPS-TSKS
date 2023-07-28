package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MultiMatchScope;
import org.khasanof.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 09.07.2023 16:01
 */
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleAudios {

    HandleAudio[] values();

    MultiMatchScope match() default MultiMatchScope.ANY_MATCH;

}
