package org.khasanof.springbootstarterfluent.core.executors;

import org.khasanof.springbootstarterfluent.core.collector.Collector;
import org.khasanof.springbootstarterfluent.core.custom.FluentContext;
import org.khasanof.springbootstarterfluent.core.model.MethodArgs;
import org.khasanof.springbootstarterfluent.core.utils.MethodUtils;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;

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

    protected final Collector collector;

    public AbstractExecutor(Collector collector) {
        this.collector = collector;
    }

}
