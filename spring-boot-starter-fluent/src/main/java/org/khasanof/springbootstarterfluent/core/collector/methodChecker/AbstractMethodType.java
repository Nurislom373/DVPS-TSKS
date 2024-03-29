package org.khasanof.springbootstarterfluent.core.collector.methodChecker;

import java.lang.annotation.Annotation;

/**
 * @author Nurislom
 * @see org.khasanof.core.collector.methodChecker
 * @since 20.07.2023 10:34
 */
public interface AbstractMethodType {

    Class<? extends Annotation> getType();

    boolean hasSuperAnnotation();

    boolean hasAny();

}
