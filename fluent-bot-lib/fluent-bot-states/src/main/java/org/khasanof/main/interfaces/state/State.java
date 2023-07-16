package org.khasanof.main.interfaces.state;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 12.07.2023 22:07
 */
public interface State {

    String getState();

    String previousState();

    void nextState(String nextState);

}
