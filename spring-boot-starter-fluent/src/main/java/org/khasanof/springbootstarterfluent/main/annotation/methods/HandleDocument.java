package org.khasanof.springbootstarterfluent.main.annotation.methods;

import org.khasanof.springbootstarterfluent.core.enums.scopes.DocumentScope;
import org.khasanof.springbootstarterfluent.core.enums.MatchScope;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;

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
@ProcessFile
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleDocument {

    String value();

    MatchScope match() default MatchScope.EQUALS;

    DocumentScope scope() default DocumentScope.FILE_NAME;

}
