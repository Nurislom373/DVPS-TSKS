package org.khasanof.core.collector;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 12:29
 * <br/>
 * Package: org.khasanof.core.collector
 */
public interface Collector {

    Map.Entry<Method, Class> getMethodValueAnn(String value, Class<? extends Annotation> annotation);

}