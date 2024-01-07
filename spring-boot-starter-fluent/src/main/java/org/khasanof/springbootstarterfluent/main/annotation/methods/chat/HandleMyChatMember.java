package org.khasanof.springbootstarterfluent.main.annotation.methods.chat;

import org.khasanof.springbootstarterfluent.main.annotation.process.ProcessUpdate;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author Nurislom
 * @see org.khasanof
 * @since 1/2/2024 6:24 PM
 */
@ProcessUpdate
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface HandleMyChatMember {
}
