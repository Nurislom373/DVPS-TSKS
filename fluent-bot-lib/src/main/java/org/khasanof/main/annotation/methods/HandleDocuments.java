package org.khasanof.main.annotation.methods;

import org.khasanof.main.annotation.extra.TGPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 04.07.2023 0:33
 */
@TGPermission
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleDocuments {

    HandleDocument[] values();

}
