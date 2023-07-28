package org.khasanof.main.inferaces.state;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 12.07.2023 22:07
 */
public interface State<T> {

    T getState();

    void nextState();

}
