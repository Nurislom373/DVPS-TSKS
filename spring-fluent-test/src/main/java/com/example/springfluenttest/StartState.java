package com.example.springfluenttest;

import org.khasanof.springbootstarterfluent.core.state.StateActions;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see com.example.springfluenttest
 * @since 8/15/2023 9:30 PM
 */
public class StartState implements StateActions<State> {

    @Override
    public State state() {
        return State.START;
    }

    @Override
    public void onReceive(Update update, AbsSender sender) {

    }
}
