package org.khasanof.main.interfaces.state;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 19:53
 */
public interface StateService {

    void registerState();

    boolean checkState(String value);

}
