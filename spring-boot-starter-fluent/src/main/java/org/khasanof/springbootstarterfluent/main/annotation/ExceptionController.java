package org.khasanof.springbootstarterfluent.main.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author Nurislom
 * @see org.khasanof.main.annotation.exception
 * @since 04.07.2023 21:37
 */
@Component
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ExceptionController {}
