package org.khasanof.springbootstarterfluent.main.annotation.methods;


import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessFile;
import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.methods
 * @since 04.07.2023 0:33
 */
@ProcessFile
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleDocuments {

    HandleDocument[] values();

}
