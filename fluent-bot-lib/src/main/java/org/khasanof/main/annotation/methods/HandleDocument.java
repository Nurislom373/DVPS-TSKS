package org.khasanof.main.annotation.methods;

import org.khasanof.core.enums.scopes.DocumentScope;
import org.khasanof.core.enums.MatchScope;
import org.khasanof.main.annotation.process.ProcessUpdate;

import java.lang.annotation.*;

/**
 * Author: Nurislom
 * <br/>
 * Date: 21.06.2023
 * <br/>
 * Time: 23:41
 * <br/>
 *
 * Package: org.khasanof.main.annotation
 */
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleDocument {

    String value();

    MatchScope match() default MatchScope.EQUALS;

    DocumentScope scope() default DocumentScope.FILE_NAME;

}
