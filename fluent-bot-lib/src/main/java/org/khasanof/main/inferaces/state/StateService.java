package org.khasanof.main.inferaces.state;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 19:53
 */
public interface StateService {

    void registerState();

    Class<? extends Enum> getType();

}
