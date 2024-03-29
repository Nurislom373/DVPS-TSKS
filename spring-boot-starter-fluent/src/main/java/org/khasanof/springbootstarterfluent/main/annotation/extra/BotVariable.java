package org.khasanof.springbootstarterfluent.main.annotation.extra;

import java.lang.annotation.*;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.extra
 * @since 30.07.2023 19:32
 */
@Documented
@Target({ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BotVariable {

    String value();

}
