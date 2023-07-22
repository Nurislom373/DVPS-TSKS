package org.khasanof.main.annotation.methods;

import org.khasanof.main.annotation.extra.TGPermission;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Nurislom
 * <br/>
 * Date: 22.06.2023
 * <br/>
 * Time: 20:34
 * <br/>
 * Package: org.khasanof.main.annotation
 */
@TGPermission
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleMessages {

    HandleMessage[] values();

}
