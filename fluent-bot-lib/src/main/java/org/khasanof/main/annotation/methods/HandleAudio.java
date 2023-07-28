package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.enums.scopes.AudioScope;
import org.khasanof.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 09.07.2023 15:56
 */
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleAudio {

    String value();

    MatchScope match() default MatchScope.EQUALS;

    AudioScope scope() default AudioScope.FILE_NAME;

}
