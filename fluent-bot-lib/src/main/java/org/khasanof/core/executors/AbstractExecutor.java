package org.khasanof.core.executors;

import org.khasanof.core.collector.Collector;
import org.khasanof.core.collector.impls.SimpleCollector;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 13:01
 * <br/>
 * Package: org.khasanof.core.executors
 */
public abstract class AbstractExecutor {

    protected final Collector collector = new SimpleCollector();

    protected void invoke(Map.Entry<Method, Class> entry, Object... args) {
        try {
            Method method = entry.getKey();
            method.setAccessible(true);
            method.invoke(entry.getValue().newInstance(), args);
        } catch (IllegalAccessException | InvocationTargetException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
