package org.khasanof.core.enums;

import org.khasanof.main.annotation.methods.HandleMessages;

import java.lang.annotation.Annotation;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 16:28
 * <br/>
 * Package: org.khasanof.core.enums
 */
public enum ExecutorType {
    CALLBACK, MESSAGE, CALLBACKS, MESSAGES;

    public static ExecutorType classToType(Class<? extends Annotation> clazz) {
        if (clazz.equals(HandleMessages.class)) {
            return MESSAGE;
        } else {
            return CALLBACK;
        }
    }

}
