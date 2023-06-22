package org.khasanof.core.executors.matcher;

import java.lang.annotation.Annotation;

/**
 * Author: Nurislom
 * <br/>
 * Date: 20.06.2023
 * <br/>
 * Time: 21:59
 * <br/>
 * Package: org.khasanof.core.executors.matcher
 */
public abstract class GenericMatcher<T extends Annotation> {

    public abstract boolean matcher(T annotation, String value);

}