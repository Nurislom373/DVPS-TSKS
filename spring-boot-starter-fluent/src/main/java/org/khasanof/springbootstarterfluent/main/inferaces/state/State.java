package org.khasanof.springbootstarterfluent.main.inferaces.state;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 12.07.2023 22:07
 */
public interface State {

    Enum getState();

    void nextState();

    void nextState(Enum state);

}
