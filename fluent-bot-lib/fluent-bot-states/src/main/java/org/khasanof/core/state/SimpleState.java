package org.khasanof.core.state;

import org.khasanof.main.inferaces.state.State;

/**
 * @author Nurislom
 * @see org.khasanof.core.state
 * @since 12.07.2023 22:11
 */
public class SimpleState implements State {

    private final StateCore stateCore;

    public SimpleState(StateCore stateCore) {
        this.stateCore = stateCore;
    }

    @Override
    public String getState() {
        return stateCore.getCurrentState();
    }

    @Override
    public String previousState() {
        return stateCore.getPrevState();
    }

    @Override
    public void nextState(String nextState) {
        stateCore.setCurrentState(nextState);
    }
}
