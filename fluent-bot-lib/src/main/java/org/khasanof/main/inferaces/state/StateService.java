package org.khasanof.main.inferaces.state;

import org.khasanof.core.state.SimpleStateService;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 09.07.2023 19:53
 */
public interface StateService {

    void registerState();

    boolean checkState(String value);

    static StateService getInstance() {
        return SimpleStateService.getInstance();
    }

}
