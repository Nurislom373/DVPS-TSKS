package org.khasanof.springbootstarterfluent.main.annotation.methods;

import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.core.enums.scopes.PhotoScope;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 06.07.2023 21:41
 */
@ProcessFile
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlePhoto {

    String value();

    MatchScope match() default MatchScope.EQUALS;

    PhotoScope scope() default PhotoScope.FILE_SIZE;

}
