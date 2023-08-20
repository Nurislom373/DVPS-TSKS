package org.khasanof.springbootstarterfluent.core.collector.loader;

import java.lang.annotation.Annotation;
import java.util.Map;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.collector
 * @since 8/4/2023 9:03 PM
 */
public interface ResourceLoader {

    Map<String, Object> getBeans();

    <T> Map<String, T> getBeansOfType(Class<T> type);

    Map<String, Object> getBeans(Class<? extends Annotation> annotation);

}
