package org.khasanof.springbootstarterfluent.core.model;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/10/2023 8:01 AM
 */
public interface AdditionalParam<T, R> extends AdditionalType {

    R getParam(T var);

    Class<?> getReturnType();

}
