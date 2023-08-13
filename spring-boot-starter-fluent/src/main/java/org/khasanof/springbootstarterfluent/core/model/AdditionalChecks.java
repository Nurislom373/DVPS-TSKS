package org.khasanof.springbootstarterfluent.core.model;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/13/2023 6:47 PM
 */
public interface AdditionalChecks<T> extends AdditionalType {

    boolean check(T var);

}
