package com.example.springfluenttest.state;

import org.khasanof.springbootstarterfluent.core.state.StateActions;
import org.khasanof.springbootstarterfluent.main.inferaces.state.State;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see com.example.springfluenttest.state
 * @since 8/30/2023 11:48 PM
 */
//@Component
public class FakeState implements StateActions<com.example.springfluenttest.FakeState> {

    @Override
    public void onReceive(Update update, AbsSender sender, State state) throws Exception {
        String text = "I'm fake state method : " + update.getMessage().getText();
        SendMessage message = new SendMessage(update.getMessage().getChatId().toString(), text);
        sender.execute(message);
        state.nextState();
    }

    @Override
    public com.example.springfluenttest.FakeState state() {
        return com.example.springfluenttest.FakeState.FAKE;
    }
}
