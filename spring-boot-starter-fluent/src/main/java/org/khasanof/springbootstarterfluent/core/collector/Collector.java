package org.khasanof.springbootstarterfluent.core.collector;

import org.khasanof.springbootstarterfluent.core.model.InvokerResult;

/**
 * Author: Nurislom
 * <br/>
 * Date: 18.06.2023
 * <br/>
 * Time: 12:29
 * <br/>
 * Package: org.khasanof.springbootstarterfluent.core.collector
 */
public interface Collector<P> extends HandleTypeCollector {

    InvokerResult getInvokerResult(Object value, P param);

    boolean hasHandle(P param);

}
