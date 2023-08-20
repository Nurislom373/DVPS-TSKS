package org.khasanof.springbootstarterfluent.core.model;

/**
 * @author Nurislom
 * @see org.khasanof.springbootstarterfluent.core.model
 * @since 8/19/2023 6:32 PM
 */
public interface Condition<P> {

    boolean match(P var);

}
