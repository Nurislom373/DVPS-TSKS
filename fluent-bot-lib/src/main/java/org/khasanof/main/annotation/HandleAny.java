package org.khasanof.main.annotation;

import org.khasanof.core.enums.HandleType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="#">Nurislom</a>
 * @since 20.06.2023
 * <p>
 *     Package: {@link org.khasanof.main.annotation}
 * </p>
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleAny {

    HandleType type();

}
