package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.MatchScope;
import org.khasanof.core.enums.scopes.PhotoScope;
import org.khasanof.main.annotation.extra.TGPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 06.07.2023 21:41
 */
@TGPermission
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandlePhoto {

    String value();

    MatchScope match() default MatchScope.EQUALS;

    PhotoScope scope() default PhotoScope.FILE_SIZE;

}
